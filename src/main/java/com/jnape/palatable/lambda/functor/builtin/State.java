package com.jnape.palatable.lambda.functor.builtin;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.recursion.RecursiveResult;
import com.jnape.palatable.lambda.functions.specialized.Pure;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.monad.MonadReader;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monad.MonadWriter;
import com.jnape.palatable.lambda.monad.transformer.builtin.StateT;

import static com.jnape.palatable.lambda.functor.builtin.Identity.pureIdentity;
import static com.jnape.palatable.lambda.monad.transformer.builtin.StateT.pureStateT;
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
public final class State<S, A> implements
        MonadRec<A, State<S, ?>>,
        MonadReader<S, A, State<S, ?>>,
        MonadWriter<S, A, State<S, ?>> {

    private final StateT<S, Identity<?>, A> delegate;

    private State(StateT<S, Identity<?>, A> delegate) {
        this.delegate = delegate;
    }

    /**
     * Convert this {@link State} to a {@link StateT} with an {@link Identity} embedding.
     *
     * @return the {@link StateT}
     */
    public StateT<S, Identity<?>, A> toStateT() {
        return delegate;
    }

    /**
     * Run the stateful computation, returning a {@link Tuple2} of the result and the final state.
     *
     * @param s the initial state
     * @return a {@link Tuple2} of the result and the final state.
     */
    public Tuple2<A, S> run(S s) {
        return delegate.<Identity<Tuple2<A, S>>>runStateT(s).runIdentity();
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
        return state(delegate.mapStateT(f -> f.fmap(fn), pureIdentity()));
    }

    /**
     * Map the final state to a new final state using the provided function.
     *
     * @param fn the state-mapping function
     * @return the mapped {@link State}
     */
    public State<S, A> withState(Fn1<? super S, ? extends S> fn) {
        return state(delegate.withStateT(fn.fmap(Identity::new)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public State<S, A> local(Fn1<? super S, ? extends S> fn) {
        return state(delegate.local(fn));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> State<S, Tuple2<A, B>> listens(Fn1<? super S, ? extends B> fn) {
        return state(delegate.listens(fn));
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
        return state(delegate.flatMap(f.fmap(state -> state.<State<S, B>>coerce().delegate)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> State<S, B> pure(B b) {
        return state(delegate.pure(b));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> State<S, B> fmap(Fn1<? super A, ? extends B> fn) {
        return MonadRec.super.<B>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> State<S, B> zip(Applicative<Fn1<? super A, ? extends B>, State<S, ?>> appFn) {
        return MonadRec.super.zip(appFn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Lazy<State<S, B>> lazyZip(
            Lazy<? extends Applicative<Fn1<? super A, ? extends B>, State<S, ?>>> lazyAppFn) {
        return MonadRec.super.lazyZip(lazyAppFn).fmap(Monad<B, State<S, ?>>::coerce);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> State<S, A> discardR(Applicative<B, State<S, ?>> appB) {
        return MonadRec.super.discardR(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> State<S, B> discardL(Applicative<B, State<S, ?>> appB) {
        return MonadRec.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> State<S, B> trampolineM(Fn1<? super A, ? extends MonadRec<RecursiveResult<A, B>, State<S, ?>>> fn) {
        return state(delegate.trampolineM(a -> fn.apply(a).<State<S, RecursiveResult<A, B>>>coerce().delegate));
    }

    /**
     * Create a {@link State} that simply returns back the initial state as both the result and the final state
     *
     * @param <A> the state and result type
     * @return the new {@link State} instance
     */
    public static <A> State<A, A> get() {
        return state(StateT.get(pureIdentity()));
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
        return state(StateT.put(new Identity<>(s)));
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
        return state(StateT.gets(a -> new Identity<>(fn.apply(a)), pureIdentity()));
    }

    /**
     * Create a {@link State} that maps its initial state into its final state, returning a {@link Unit} result type.
     *
     * @param fn  the mapping function
     * @param <S> the state type
     * @return the new {@link State} instance
     */
    public static <S> State<S, Unit> modify(Fn1<? super S, ? extends S> fn) {
        return state(StateT.modify(s -> new Identity<>(fn.apply(s)), pureIdentity()));
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
        return state(stateT(new Identity<>(a)));
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
        return state(stateT(s -> new Identity<>(stateFn.apply(s)), pureIdentity()));
    }

    /**
     * The canonical {@link Pure} instance for {@link State}.
     *
     * @param <S> the state type
     * @return the {@link Pure} instance
     */
    public static <S> Pure<State<S, ?>> pureState() {
        Pure<StateT<S, Identity<?>, ?>> pureStateT = pureStateT(pureIdentity());
        return new Pure<State<S, ?>>() {
            @Override
            public <A> State<S, A> checkedApply(A a) {
                return state(pureStateT.<A, StateT<S, Identity<?>, A>>apply(a));
            }
        };
    }

    /**
     * Create a {@link State} from a delegate {@link StateT} with an {@link Identity} embedding.
     *
     * @param stateT the delegate {@link StateT}
     * @param <S>    the state type
     * @param <A>    the result type
     * @return the new {@link State}
     */
    public static <S, A> State<S, A> state(StateT<S, Identity<?>, A> stateT) {
        return new State<>(stateT);
    }
}
