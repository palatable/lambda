package com.jnape.palatable.lambda.monad.transformer.builtin;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.recursion.RecursiveResult;
import com.jnape.palatable.lambda.functions.specialized.Lift;
import com.jnape.palatable.lambda.functions.specialized.Pure;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Cartesian;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.monad.MonadReader;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monad.transformer.MonadT;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Tupler2.tupler;

/**
 * A {@link MonadT monad transformer} for any {@link Fn1 function} from some type <code>R</code> to some
 * {@link MonadRec monadic} embedding <code>{@link MonadRec}&lt;A, M&gt;</code>.
 *
 * @param <R> the input type
 * @param <M> the returned {@link MonadRec}
 * @param <A> the embedded output type
 */
public final class ReaderT<R, M extends MonadRec<?, M>, A> implements
        MonadReader<R, A, ReaderT<R, M, ?>>,
        Cartesian<R, A, ReaderT<?, M, ?>>,
        MonadT<M, A, ReaderT<R, M, ?>, ReaderT<R, ?, ?>> {

    private final Fn1<? super R, ? extends MonadRec<A, M>> f;

    private ReaderT(Fn1<? super R, ? extends MonadRec<A, M>> f) {
        this.f = f;
    }

    /**
     * Run the computation represented by this {@link ReaderT}.
     *
     * @param r    the input
     * @param <MA> the witnessed target type
     * @return the embedded {@link MonadRec}
     */
    public <MA extends MonadRec<A, M>> MA runReaderT(R r) {
        return f.apply(r).coerce();
    }

    /**
     * Map the current {@link Monad monadic} embedding to a new one in a potentially different {@link Monad}.
     *
     * @param fn   the function
     * @param <MA> the currently embedded {@link Monad}
     * @param <N>  the new {@link Monad} witness
     * @param <B>  the new carrier type
     * @return the mapped {@link ReaderT}
     */
    public <MA extends MonadRec<A, M>, N extends MonadRec<?, N>, B> ReaderT<R, N, B> mapReaderT(
            Fn1<? super MA, ? extends MonadRec<B, N>> fn) {
        return readerT(r -> fn.apply(runReaderT(r).coerce()));
    }

    /**
     * Left-to-right composition between {@link ReaderT} instances running under the same effect and compatible between
     * their inputs and outputs.
     *
     * @param amb the next {@link ReaderT} to run
     * @param <B> the final output type
     * @return the composed {@link ReaderT}
     */
    public <B> ReaderT<R, M, B> and(ReaderT<A, M, B> amb) {
        return readerT(r -> runReaderT(r).flatMap(amb::runReaderT));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReaderT<R, M, A> local(Fn1<? super R, ? extends R> fn) {
        return contraMap(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B, N extends MonadRec<?, N>> ReaderT<R, N, B> lift(MonadRec<B, N> mb) {
        return ReaderT.<R>liftReaderT().apply(mb);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> ReaderT<R, M, B> flatMap(Fn1<? super A, ? extends Monad<B, ReaderT<R, M, ?>>> f) {
        return readerT(r -> runReaderT(r).flatMap(a -> f.apply(a).<ReaderT<R, M, B>>coerce().runReaderT(r)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> ReaderT<R, M, B> trampolineM(
            Fn1<? super A, ? extends MonadRec<RecursiveResult<A, B>, ReaderT<R, M, ?>>> fn) {
        return readerT(r -> runReaderT(r).trampolineM(a -> fn.apply(a).<ReaderT<R, M, RecursiveResult<A, B>>>coerce()
                .runReaderT(r)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> ReaderT<R, M, B> pure(B b) {
        return readerT(r -> runReaderT(r).pure(b));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> ReaderT<R, M, B> fmap(Fn1<? super A, ? extends B> fn) {
        return readerT(r -> runReaderT(r).fmap(fn));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> ReaderT<R, M, B> zip(Applicative<Fn1<? super A, ? extends B>, ReaderT<R, M, ?>> appFn) {
        return readerT(r -> f.apply(r).zip(appFn.<ReaderT<R, M, Fn1<? super A, ? extends B>>>coerce().runReaderT(r)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Lazy<ReaderT<R, M, B>> lazyZip(
            Lazy<? extends Applicative<Fn1<? super A, ? extends B>, ReaderT<R, M, ?>>> lazyAppFn) {
        return lazyAppFn.fmap(this::zip);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> ReaderT<R, M, B> discardL(Applicative<B, ReaderT<R, M, ?>> appB) {
        return MonadT.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> ReaderT<R, M, A> discardR(Applicative<B, ReaderT<R, M, ?>> appB) {
        return MonadT.super.discardR(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <Q, B> ReaderT<Q, M, B> diMap(Fn1<? super Q, ? extends R> lFn, Fn1<? super A, ? extends B> rFn) {
        return readerT(q -> runReaderT(lFn.apply(q)).fmap(rFn));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <Q> ReaderT<Q, M, A> diMapL(Fn1<? super Q, ? extends R> fn) {
        return (ReaderT<Q, M, A>) Cartesian.super.<Q>diMapL(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> ReaderT<R, M, B> diMapR(Fn1<? super A, ? extends B> fn) {
        return (ReaderT<R, M, B>) Cartesian.super.<B>diMapR(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <Q> ReaderT<Q, M, A> contraMap(Fn1<? super Q, ? extends R> fn) {
        return (ReaderT<Q, M, A>) Cartesian.super.<Q>contraMap(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <C> ReaderT<Tuple2<C, R>, M, Tuple2<C, A>> cartesian() {
        return readerT(into((c, r) -> runReaderT(r).fmap(tupler(c))));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReaderT<R, M, Tuple2<R, A>> carry() {
        return (ReaderT<R, M, Tuple2<R, A>>) Cartesian.super.carry();
    }

    /**
     * Given a {@link Pure} ask will give you access to the input within the monadic embedding
     *
     * @param pureM the {@link Pure} instance for the given {@link Monad}
     * @param <R> the input and output type of the returned ReaderT
     * @param <M> the returned {@link Monad}
     * @return the {@link ReaderT}
     */
    public static <R, M extends MonadRec<?, M>> ReaderT<R, M, R> ask(Pure<M> pureM) {
        //noinspection Convert2MethodRef
        return readerT(a -> pureM.apply(a));
    }

    /**
     * Lift a {@link Fn1 function} (<code>R -&gt; {@link Monad}&lt;A, M&gt;</code>) into a {@link ReaderT} instance.
     *
     * @param fn  the function
     * @param <R> the input type
     * @param <M> the returned {@link Monad}
     * @param <A> the embedded output type
     * @return the {@link ReaderT}
     */
    public static <R, M extends MonadRec<?, M>, A> ReaderT<R, M, A> readerT(
            Fn1<? super R, ? extends MonadRec<A, M>> fn) {
        return new ReaderT<>(fn);
    }

    /**
     * The canonical {@link Pure} instance for {@link ReaderT}.
     *
     * @param pureM the argument {@link Monad} {@link Pure}
     * @param <R>   the input type
     * @param <M>   the argument {@link Monad} witness
     * @return the {@link Pure} instance
     */
    public static <R, M extends MonadRec<?, M>> Pure<ReaderT<R, M, ?>> pureReaderT(Pure<M> pureM) {
        return new Pure<ReaderT<R, M, ?>>() {
            @Override
            public <A> ReaderT<R, M, A> checkedApply(A a) {
                return readerT(__ -> pureM.apply(a));
            }
        };
    }

    /**
     * {@link Lift} for {@link ReaderT}.
     *
     * @param <R> the environment type
     * @return the {@link Monad} lifted into {@link ReaderT}
     */
    public static <R> Lift<ReaderT<R, ?, ?>> liftReaderT() {
        return new Lift<ReaderT<R, ?, ?>>() {
            @Override
            public <A, M extends MonadRec<?, M>> ReaderT<R, M, A> checkedApply(MonadRec<A, M> ga) {
                return readerT(constantly(ga));
            }
        };
    }
}
