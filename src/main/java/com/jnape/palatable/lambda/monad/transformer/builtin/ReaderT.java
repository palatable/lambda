package com.jnape.palatable.lambda.monad.transformer.builtin;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.Pure;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Cartesian;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.monad.transformer.MonadT;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Tupler2.tupler;

/**
 * A {@link MonadT monad transformer} for any {@link Fn1 function} from some type <code>R</code> to some
 * {@link Monad monadic} embedding <code>{@link Monad}&lt;A, M&gt;</code>.
 *
 * @param <R> the input type
 * @param <M> the returned {@link Monad}
 * @param <A> the embedded output type
 */
public final class ReaderT<R, M extends Monad<?, M>, A> implements
        MonadT<Fn1<R, ?>, M, A, ReaderT<R, M, ?>>,
        Cartesian<R, A, ReaderT<?, M, ?>> {

    private final Fn1<? super R, ? extends Monad<A, M>> fn;

    private ReaderT(Fn1<? super R, ? extends Monad<A, M>> fn) {
        this.fn = fn;
    }

    /**
     * Run the computation represented by this {@link ReaderT}.
     *
     * @param r the input
     * @return the {¬@link Monad monadic} embedding {@link Monad}&lt;A, M&gt;
     */
    public <MA extends Monad<A, M>> MA runReaderT(R r) {
        return fn.apply(r).coerce();
    }

    /**
     * Map the current {@link Monad monadic} embedding to a new one in a potentially different {@link Monad}.
     *
     * @param fn   the mapping function
     * @param <MA> the inference target of the current {@link Monad monadic} embedding
     * @param <N>  the new {@link Monad} to embed the result in
     * @param <B>  the new embedded result
     * @return the mapped {@link ReaderT}
     */
    public <MA extends Monad<A, M>, N extends Monad<?, N>, B> ReaderT<R, N, B> mapReaderT(
            Fn1<? super MA, ? extends Monad<B, N>> fn) {
        return readerT(r -> fn.apply(runReaderT(r).coerce()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <GA extends Monad<A, M>, FGA extends Monad<GA, Fn1<R, ?>>> FGA run() {
        return Fn1.<R, GA>fn1(r -> runReaderT(r).coerce()).coerce();
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
    public <B> ReaderT<R, M, B> pure(B b) {
        return readerT(r -> runReaderT(r).pure(b));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> ReaderT<R, M, B> fmap(Fn1<? super A, ? extends B> fn) {
        return MonadT.super.<B>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> ReaderT<R, M, B> zip(Applicative<Fn1<? super A, ? extends B>, ReaderT<R, M, ?>> appFn) {
        return readerT(r -> runReaderT(r).zip(appFn.<ReaderT<R, M, Fn1<? super A, ? extends B>>>coerce().runReaderT(r)));
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
     * Lift a {@link Fn1 function} (<code>R -&gt; {@link Monad}&lt;A, M&gt;</code>) into a {@link ReaderT} instance.
     *
     * @param fn  the function
     * @param <R> the input type
     * @param <M> the returned {@link Monad}
     * @param <A> the embedded output type
     * @return the {@link ReaderT}
     */
    public static <R, M extends Monad<?, M>, A> ReaderT<R, M, A> readerT(Fn1<? super R, ? extends Monad<A, M>> fn) {
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
    public static <R, M extends Monad<?, M>> Pure<ReaderT<R, M, ?>> pureReaderT(Pure<M> pureM) {
        return new Pure<ReaderT<R, M, ?>>() {
            @Override
            public <A> ReaderT<R, M, A> checkedApply(A a) {
                return readerT(__ -> pureM.apply(a));
            }
        };
    }
}
