package com.jnape.palatable.lambda.functor.builtin;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.Pure;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.monad.MonadReader;
import com.jnape.palatable.lambda.monad.MonadWriter;
import com.jnape.palatable.lambda.monad.transformer.builtin.StateT;

import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Both.both;
import static com.jnape.palatable.lambda.monad.transformer.builtin.StateT.stateT;

/**
 * The state {@link Monad}, useful for iteratively building up state and state-contextualized result.
 * <p>
 * For more information, read about the
 * <a href="https://en.wikibooks.org/wiki/Haskell/Understanding_monads/State" target="_blank">state monad</a>.
 *
 * @param <S> the state type
 * @param <A> the result type
 */
public final class State<S, A> implements MonadReader<S, A, State<S, ?>>, MonadWriter<S, A, State<S, ?>> {

    private final StateT<S, Identity<?>, A> stateFn;

    private State(StateT<S, Identity<?>, A> stateFn) {
        this.stateFn = stateFn;
    }

    /**
     * Run the stateful computation, returning a {@link Tuple2} of the result and the final state.
     *
     * @param s the initial state
     * @return a {@link Tuple2} of the result and the final state.
     */
    public Tuple2<A, S> run(S s) {
        return stateFn.<Identity<Tuple2<A, S>>>runStateT(s).runIdentity();
    }

    /**
     * Run the stateful computation, returning the result.
     *
     * @param s the initial state
     * @return the result
     */
    public A eval(S s) {
        return run(s)._1();
    }

    /**
     * Run the stateful computation, returning the final state.
     *
     * @param s the initial state
     * @return the final state
     */
    public S exec(S s) {
        return run(s)._2();
    }

    /**
     * Map both the result and the final state to a new result and final state.
     *
     * @param fn  the mapping function
     * @param <B> the new state type
     * @return the mapped {@link State}
     */
    public <B> State<S, B> mapState(Fn1<? super Tuple2<A, S>, ? extends Tuple2<B, S>> fn) {
        return state(s -> fn.apply(run(s)));
    }

    /**
     * Map the final state to a new final state using the provided function.
     *
     * @param fn the state-mapping function
     * @return the mapped {@link State}
     */
    public State<S, A> withState(Fn1<? super S, ? extends S> fn) {
        return state(s -> run(fn.apply(s)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public State<S, A> local(Fn1<? super S, ? extends S> fn) {
        return state(s -> run(fn.apply(s)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> State<S, Tuple2<A, B>> listens(Fn1<? super S, ? extends B> fn) {
        return state(s -> run(s).biMapL(both(id(), constantly(fn.apply(s)))));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public State<S, A> censor(Fn1<? super S, ? extends S> fn) {
        return local(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> State<S, B> flatMap(Fn1<? super A, ? extends Monad<B, State<S, ?>>> f) {
        return state(s -> run(s).into((a, s2) -> f.apply(a).<State<S, B>>coerce().run(s2)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> State<S, B> pure(B b) {
        return state(s -> tuple(b, s));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> State<S, B> fmap(Fn1<? super A, ? extends B> fn) {
        return MonadReader.super.<B>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> State<S, B> zip(Applicative<Fn1<? super A, ? extends B>, State<S, ?>> appFn) {
        return MonadReader.super.zip(appFn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Lazy<State<S, B>> lazyZip(
            Lazy<? extends Applicative<Fn1<? super A, ? extends B>, State<S, ?>>> lazyAppFn) {
        return MonadReader.super.lazyZip(lazyAppFn).fmap(Monad<B, State<S, ?>>::coerce);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> State<S, A> discardR(Applicative<B, State<S, ?>> appB) {
        return MonadReader.super.discardR(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> State<S, B> discardL(Applicative<B, State<S, ?>> appB) {
        return MonadReader.super.discardL(appB).coerce();
    }

    /**
     * Create a {@link State} that simply returns back the initial state as both the result and the final state
     *
     * @param <A> the state and result type
     * @return the new {@link State} instance
     */
    public static <A> State<A, A> get() {
        return state(Tuple2::fill);
    }

    /**
     * Create a {@link State} that ignores its initial state, returning a {@link Unit} result and <code>s</code> as its
     * final state.
     *
     * @param s   the final state
     * @param <S> the state type
     * @return the new {@link State} instance
     */
    public static <S> State<S, Unit> put(S s) {
        return modify(constantly(s));
    }

    /**
     * Create a {@link State} that maps its initial state into its result, but leaves the initial state unchanged.
     *
     * @param fn  the mapping function
     * @param <S> the state type
     * @param <A> the result type
     * @return the new {@link State} instance
     */
    public static <S, A> State<S, A> gets(Fn1<? super S, ? extends A> fn) {
        return state(both(fn, id()));
    }

    /**
     * Create a {@link State} that maps its initial state into its final state, returning a {@link Unit} result type.
     *
     * @param fn  the mapping function
     * @param <S> the state type
     * @return the new {@link State} instance
     */
    public static <S> State<S, Unit> modify(Fn1<? super S, ? extends S> fn) {
        return state(both(constantly(UNIT), fn));
    }

    /**
     * Create a {@link State} that returns <code>a</code> as its result and its initial state as its final state.
     *
     * @param a   the result
     * @param <S> the state type
     * @param <A> the result type
     * @return the new {@link State} instance
     */
    public static <S, A> State<S, A> state(A a) {
        return gets(constantly(a));
    }

    /**
     * Create a {@link State} from <code>stateFn</code>, a function that maps an initial state into a result and a final
     * state.
     *
     * @param stateFn the state function
     * @param <S>     the state type
     * @param <A>     the result type
     * @return the new {@link State} instance
     */
    public static <S, A> State<S, A> state(Fn1<? super S, ? extends Tuple2<A, S>> stateFn) {
        return new State<>(stateT(s -> new Identity<>(stateFn.apply(s))));
    }

    /**
     * The canonical {@link Pure} instance for {@link State}.
     *
     * @param <S> the state type
     * @return the {@link Pure} instance
     */
    public static <S> Pure<State<S, ?>> pureState() {
        return new Pure<State<S, ?>>() {
            @Override
            public <A> State<S, A> checkedApply(A a) throws Throwable {
                return state(s -> tuple(a, s));
            }
        };
    }
}
