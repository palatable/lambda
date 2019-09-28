package com.jnape.palatable.lambda.monad.transformer.builtin;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.recursion.RecursiveResult;
import com.jnape.palatable.lambda.functions.specialized.Lift;
import com.jnape.palatable.lambda.functions.specialized.Pure;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.functor.builtin.Writer;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monad.MonadWriter;
import com.jnape.palatable.lambda.monad.transformer.MonadT;
import com.jnape.palatable.lambda.monoid.Monoid;

import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Both.both;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Tupler2.tupler;

/**
 * A {@link MonadT monad transformer} for {@link Writer}.
 *
 * @param <W> the accumulation type
 * @param <M> the {@link Monad monadic embedding}
 * @param <A> the result type
 */
public final class WriterT<W, M extends MonadRec<?, M>, A> implements
        MonadWriter<W, A, WriterT<W, M, ?>>,
        MonadT<M, A, WriterT<W, M, ?>, WriterT<W, ?, ?>> {

    private final Fn1<? super Monoid<W>, ? extends MonadRec<Tuple2<A, W>, M>> writerFn;

    private WriterT(Fn1<? super Monoid<W>, ? extends MonadRec<Tuple2<A, W>, M>> writerFn) {
        this.writerFn = writerFn;
    }

    /**
     * Given a {@link Monoid} for the accumulation, run the computation represented by this {@link WriterT} inside the
     * {@link Monad monadic effect}, accumulate the written output in terms of the {@link Monoid}, and produce the
     * accumulation and the result inside the {@link Monad}.
     *
     * @param monoid the accumulation {@link Monoid}
     * @param <MAW>  the inferred {@link Monad} result
     * @return the accumulation with the result
     */
    public <MAW extends MonadRec<Tuple2<A, W>, M>> MAW runWriterT(Monoid<W> monoid) {
        return writerFn.apply(monoid).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> WriterT<W, M, Tuple2<A, B>> listens(Fn1<? super W, ? extends B> fn) {
        return new WriterT<>(writerFn.fmap(m -> m.fmap(into((a, w) -> both(both(constantly(a), fn), id(), w)))));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WriterT<W, M, A> censor(Fn1<? super W, ? extends W> fn) {
        return new WriterT<>(writerFn.fmap(mt -> mt.fmap(t -> t.fmap(fn))));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B, N extends MonadRec<?, N>> WriterT<W, N, B> lift(MonadRec<B, N> mb) {
        return WriterT.<W>liftWriterT().apply(mb);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> WriterT<W, M, B> trampolineM(
            Fn1<? super A, ? extends MonadRec<RecursiveResult<A, B>, WriterT<W, M, ?>>> fn) {
        return new WriterT<>(monoid -> runWriterT(monoid).trampolineM(into((a, w) -> fn.apply(a)
                .<WriterT<W, M, RecursiveResult<A, B>>>coerce()
                .runWriterT(monoid).fmap(t -> t.fmap(monoid.apply(w)))
                .fmap(into((aOrB, w_) -> aOrB.biMap(a_ -> tuple(a_, w_), b -> tuple(b, w_)))))));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> WriterT<W, M, B> fmap(Fn1<? super A, ? extends B> fn) {
        return MonadT.super.<B>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> WriterT<W, M, B> pure(B b) {
        return new WriterT<>(m -> runWriterT(m).pure(tuple(b, m.identity())));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> WriterT<W, M, B> flatMap(Fn1<? super A, ? extends Monad<B, WriterT<W, M, ?>>> f) {
        return new WriterT<>(monoid -> writerFn.apply(monoid)
                .flatMap(into((a, w) -> f.apply(a).<WriterT<W, M, B>>coerce().runWriterT(monoid)
                        .fmap(t -> t.fmap(monoid.apply(w))))));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> WriterT<W, M, B> zip(Applicative<Fn1<? super A, ? extends B>, WriterT<W, M, ?>> appFn) {
        return new WriterT<>(monoid -> runWriterT(monoid)
                .zip(appFn.<WriterT<W, M, Fn1<? super A, ? extends B>>>coerce().runWriterT(monoid)
                             .fmap(into((f, y) -> into((a, x) -> tuple(f.apply(a), monoid.apply(x, y)))))));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Lazy<WriterT<W, M, B>> lazyZip(
            Lazy<? extends Applicative<Fn1<? super A, ? extends B>, WriterT<W, M, ?>>> lazyAppFn) {
        return lazyAppFn.fmap(this::zip);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> WriterT<W, M, B> discardL(Applicative<B, WriterT<W, M, ?>> appB) {
        return MonadT.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> WriterT<W, M, A> discardR(Applicative<B, WriterT<W, M, ?>> appB) {
        return MonadT.super.discardR(appB).coerce();
    }

    /**
     * Lift an accumulation embedded in a {@link Monad} into a {@link WriterT}.
     *
     * @param mw  the accumulation inside a {@link Monad}
     * @param <W> the accumulation type
     * @param <M> the {@link Monad} type
     * @return the {@link WriterT}
     */
    public static <W, M extends MonadRec<?, M>> WriterT<W, M, Unit> tell(MonadRec<W, M> mw) {
        return writerT(mw.fmap(tupler(UNIT)));
    }

    /**
     * Lift a value embedded in a {@link Monad} into a {@link WriterT}.
     *
     * @param ma  the value inside a {@link Monad}
     * @param <W> the accumulation type
     * @param <M> the {@link Monad} type
     * @param <A> the value type
     * @return the {@link WriterT}
     */
    public static <W, M extends MonadRec<?, M>, A> WriterT<W, M, A> listen(MonadRec<A, M> ma) {
        return new WriterT<>(monoid -> ma.fmap(a -> tuple(a, monoid.identity())));
    }

    /**
     * Lift a value and an accumulation embedded in a {@link Monad} into a {@link WriterT}.
     *
     * @param maw the value and accumulation inside a {@link Monad}
     * @param <W> the accumulation type
     * @param <M> the {@link Monad} type
     * @param <A> the value type
     * @return the {@link WriterT}
     */
    public static <W, M extends MonadRec<?, M>, A> WriterT<W, M, A> writerT(MonadRec<Tuple2<A, W>, M> maw) {
        return new WriterT<>(constantly(maw));
    }

    /**
     * The canonical {@link Pure} instance for {@link WriterT}.
     *
     * @param pureM the argument {@link Monad} {@link Pure}
     * @param <W>   the accumulation type
     * @param <M>   the argument {@link Monad} witness
     * @return the {@link Pure} instance
     */
    public static <W, M extends MonadRec<?, M>> Pure<WriterT<W, M, ?>> pureWriterT(Pure<M> pureM) {
        return new Pure<WriterT<W, M, ?>>() {
            @Override
            public <A> WriterT<W, M, A> checkedApply(A a) {
                return listen(pureM.<A, MonadRec<A, M>>apply(a));
            }
        };
    }

    /**
     * {@link Lift} for {@link WriterT}.
     *
     * @param <W> the accumulated type
     * @return the {@link Monad} lifted into {@link WriterT}
     */
    public static <W> Lift<WriterT<W, ?, ?>> liftWriterT() {
        return WriterT::listen;
    }
}
