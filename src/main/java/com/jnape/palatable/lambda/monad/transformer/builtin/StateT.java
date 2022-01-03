package com.jnape.palatable.lambda.monad.transformer.builtin;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.recursion.RecursiveResult;
import com.jnape.palatable.lambda.functions.specialized.Lift;
import com.jnape.palatable.lambda.functions.specialized.Pure;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.functor.builtin.State;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.monad.MonadReader;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monad.MonadWriter;
import com.jnape.palatable.lambda.monad.transformer.MonadT;

import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Tupler2.tupler;


/**
 * The {@link State} {@link MonadT monad transformer}.
 *
 * @param <S> the state type
 * @param <M> the {@link Monad monadic embedding}
 * @param <A> the result type
 * @see State
 */
public final class StateT<S, M extends MonadRec<?, M>, A> implements
        MonadT<M, A, StateT<S, M, ?>, StateT<S, ?, ?>>,
        MonadReader<S, A, StateT<S, M, ?>>,
        MonadWriter<S, A, StateT<S, M, ?>> {

    private final Fn1<? super S, ? extends MonadRec<Tuple2<A, S>, M>> stateFn;

    private StateT(Fn1<? super S, ? extends MonadRec<Tuple2<A, S>, M>> stateFn) {
        this.stateFn = stateFn;
    }

    /**
     * Run the stateful computation embedded in the {@link Monad}, returning a {@link Tuple2} of the result and the
     * final state.
     *
     * @param s     the initial state
     * @param <MAS> the inferred {@link Monad} result
     * @return a {@link Tuple2} of the result and the final state.
     */
    public <MAS extends MonadRec<Tuple2<A, S>, M>> MAS runStateT(S s) {
        return stateFn.apply(s).coerce();
    }

    /**
     * Run the stateful computation embedded in the {@link Monad}, returning the result.
     *
     * @param s    the initial state
     * @param <MA> the inferred {@link Monad} result
     * @return the result
     */
    public <MA extends Monad<A, M>> MA evalT(S s) {
        return runStateT(s).fmap(Tuple2::_1).coerce();
    }

    /**
     * Run the stateful computation embedded in the {@link Monad}, returning the final state.
     *
     * @param s    the initial state
     * @param <MS> the inferred {@link Monad} result
     * @return the final state
     */
    public <MS extends Monad<S, M>> MS execT(S s) {
        return runStateT(s).fmap(Tuple2::_2).coerce();
    }

    /**
     * Map both the result and the final state to a new result and final state inside the {@link Monad}.
     *
     * @param fn  the mapping function
     * @param <N> the new {@link Monad monadic embedding} for this {@link StateT}
     * @param <B> the new state type
     * @return the mapped {@link StateT}
     */
    public <N extends MonadRec<?, N>, B> StateT<S, N, B> mapStateT(
            Fn1<? super MonadRec<Tuple2<A, S>, M>, ? extends MonadRec<Tuple2<B, S>, N>> fn) {
        return stateT(s -> fn.apply(runStateT(s)));
    }

    /**
     * Map the final state to a new final state inside the same {@link Monad monadic effect} using the provided
     * function.
     *
     * @param fn the state-mapping function
     * @return the mapped {@link StateT}
     */
    public StateT<S, M, A> withStateT(Fn1<? super S, ? extends MonadRec<S, M>> fn) {
        return modify(fn).flatMap(constantly(this));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> StateT<S, M, Tuple2<A, B>> listens(Fn1<? super S, ? extends B> fn) {
        return mapStateT(mas -> mas.fmap(t -> t.into((a, s) -> tuple(tuple(a, fn.apply(s)), s))));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StateT<S, M, A> censor(Fn1<? super S, ? extends S> fn) {
        return local(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StateT<S, M, A> local(Fn1<? super S, ? extends S> fn) {
        return stateT(s -> runStateT(fn.apply(s)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> StateT<S, M, B> flatMap(Fn1<? super A, ? extends Monad<B, StateT<S, M, ?>>> f) {
        return stateT(s -> runStateT(s).flatMap(into((a, s_) -> f.apply(a).<StateT<S, M, B>>coerce().runStateT(s_))));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> MonadReader<S, B, StateT<S, M, ?>> rflatMap(Fn2<? super S, ? super A, ? extends MonadReader<S, B, StateT<S, M, ?>>> f) {
    	return stateT(s -> runStateT(s).flatMap(into((a, s_) -> f.apply(s, a).<StateT<S, M, B>>coerce().runStateT(s_))));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> StateT<S, M, B> pure(B b) {
        return stateT(s -> runStateT(s).pure(tuple(b, s)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> StateT<S, M, B> fmap(Fn1<? super A, ? extends B> fn) {
        return stateT(s -> runStateT(s).fmap(t -> t.biMapL(fn)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> StateT<S, M, B> zip(Applicative<Fn1<? super A, ? extends B>, StateT<S, M, ?>> appFn) {
        return MonadT.super.zip(appFn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Lazy<StateT<S, M, B>> lazyZip(
            Lazy<? extends Applicative<Fn1<? super A, ? extends B>, StateT<S, M, ?>>> lazyAppFn) {
        return MonadT.super.lazyZip(lazyAppFn).fmap(MonadT<M, B, StateT<S, M, ?>, StateT<S, ?, ?>>::coerce);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> StateT<S, M, B> discardL(Applicative<B, StateT<S, M, ?>> appB) {
        return MonadT.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> StateT<S, M, A> discardR(Applicative<B, StateT<S, M, ?>> appB) {
        return MonadT.super.discardR(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B, N extends MonadRec<?, N>> StateT<S, N, B> lift(MonadRec<B, N> mb) {
        return stateT(s -> mb.fmap(b -> tuple(b, s)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> StateT<S, M, B> trampolineM(
            Fn1<? super A, ? extends MonadRec<RecursiveResult<A, B>, StateT<S, M, ?>>> fn) {
        return StateT.<S, M, B>stateT((Fn1.<S, MonadRec<Tuple2<A, S>, M>>fn1(this::runStateT))
                                              .fmap(m -> m.trampolineM(into((a, s) -> fn.apply(a)
                                                      .<StateT<S, M, RecursiveResult<A, B>>>coerce().runStateT(s)
                                                      .fmap(into((aOrB, s_) -> aOrB.biMap(a_ -> tuple(a_, s_),
                                                                                          b -> tuple(b, s_))))))));
    }

    /**
     * Given a {@link Pure pure} construction of some {@link Monad}, produce a {@link StateT} that equates its output
     * with its state.
     *
     * @param pureM the {@link Pure pure} construction
     * @param <A>   the state and value type
     * @param <M>   the {@link Monad} embedding
     * @return the {@link StateT}
     */
    @SuppressWarnings("RedundantTypeArguments")
    public static <A, M extends MonadRec<?, M>> StateT<A, M, A> get(Pure<M> pureM) {
        return gets(pureM::<A, MonadRec<A, M>>apply);
    }

    /**
     * Given a function that produces a value inside a {@link Monad monadic effect} from a state, produce a
     * {@link StateT} that simply passes its state to the function and applies it.
     *
     * @param fn  the function
     * @param <S> the state type
     * @param <M> the{@link Monad} embedding
     * @param <A> the value type
     * @return the {@link StateT}
     */
    public static <S, M extends MonadRec<?, M>, A> StateT<S, M, A> gets(Fn1<? super S, ? extends MonadRec<A, M>> fn) {
        return stateT(s -> fn.apply(s).fmap(a -> tuple(a, s)));
    }

    /**
     * Lift a function that makes a stateful modification inside an {@link Monad} into {@link StateT}.
     *
     * @param updateFn the update function
     * @param <S>      the state type
     * @param <M>      the {@link Monad} embedding
     * @return the {@link StateT}
     */
    public static <S, M extends MonadRec<?, M>> StateT<S, M, Unit> modify(
            Fn1<? super S, ? extends MonadRec<S, M>> updateFn) {
        return stateT(s -> updateFn.apply(s).fmap(tupler(UNIT)));
    }

    /**
     * Lift a {@link MonadRec monadic state} into {@link StateT}.
     *
     * @param ms  the state
     * @param <S> the state type
     * @param <M> the {@link MonadRec} embedding
     * @return the {@link StateT}
     */
    public static <S, M extends MonadRec<?, M>> StateT<S, M, Unit> put(MonadRec<S, M> ms) {
        return modify(constantly(ms));
    }

    /**
     * Lift a {@link MonadRec monadic value} into {@link StateT}.
     *
     * @param ma  the value
     * @param <S> the state type
     * @param <M> the {@link Monad} embedding
     * @param <A> the result type
     * @return the {@link StateT}
     */
    public static <S, M extends MonadRec<?, M>, A> StateT<S, M, A> stateT(MonadRec<A, M> ma) {
        return gets(constantly(ma));
    }

    /**
     * Lift a state-sensitive {@link Monad monadically embedded} computation into {@link StateT}.
     *
     * @param stateFn the stateful operation
     * @param <S>     the state type
     * @param <M>     the {@link Monad} embedding
     * @param <A>     the result type
     * @return the {@link StateT}
     */
    public static <S, M extends MonadRec<?, M>, A> StateT<S, M, A> stateT(
            Fn1<? super S, ? extends MonadRec<Tuple2<A, S>, M>> stateFn) {
        return new StateT<>(stateFn);
    }

    /**
     * The canonical {@link Pure} instance for {@link StateT}.
     *
     * @param pureM the argument {@link Monad} {@link Pure}
     * @param <S>   the state type
     * @param <M>   the argument {@link Monad} witness
     * @return the {@link Pure} instance
     */
    public static <S, M extends MonadRec<?, M>> Pure<StateT<S, M, ?>> pureStateT(Pure<M> pureM) {
        return new Pure<StateT<S, M, ?>>() {
            @Override
            public <A> StateT<S, M, A> checkedApply(A a) {
                return stateT(pureM.<A, MonadRec<A, M>>apply(a));
            }
        };
    }

    /**
     * {@link Lift} for {@link StateT}.
     *
     * @param <S> the state type
     * @return the {@link Monad} lifted into {@link StateT}
     */
    public static <S> Lift<StateT<S, ?, ?>> liftStateT() {
        return StateT::stateT;
    }
}
