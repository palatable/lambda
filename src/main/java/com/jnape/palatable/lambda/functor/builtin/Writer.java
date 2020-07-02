package com.jnape.palatable.lambda.functor.builtin;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.recursion.RecursiveResult;
import com.jnape.palatable.lambda.functions.specialized.Pure;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monad.MonadWriter;
import com.jnape.palatable.lambda.monad.transformer.builtin.WriterT;
import com.jnape.palatable.lambda.monoid.Monoid;

import static com.jnape.palatable.lambda.functor.builtin.Identity.pureIdentity;
import static com.jnape.palatable.lambda.monad.transformer.builtin.WriterT.pureWriterT;
import static com.jnape.palatable.lambda.monad.transformer.builtin.WriterT.writerT;

/**
 * The lazy writer monad, a monad capturing some accumulation (eventually to be folded in terms of a given monoid) and
 * a value. Note that unlike the {@link State} monad, the {@link Writer} monad does not allow the value to be fully
 * derived from the accumulation.
 *
 * @param <W> the accumulation type
 * @param <A> the value type
 */
public final class Writer<W, A> implements
        MonadWriter<W, A, Writer<W, ?>>,
        MonadRec<A, Writer<W, ?>> {

    private final WriterT<W, Identity<?>, A> delegate;

    private Writer(WriterT<W, Identity<?>, A> delegate) {
        this.delegate = delegate;
    }

    /**
     * Convert this {@link Writer} to a {@link WriterT} with an {@link Identity} embedding.
     *
     * @return the {@link WriterT}
     */
    public WriterT<W, Identity<?>, A> toWriterT() {
        return delegate;
    }

    /**
     * Given a {@link Monoid} for the accumulation, run the computation represented by this {@link Writer}, accumulate
     * the written output in terms of the {@link Monoid}, and produce the accumulation and the value.
     *
     * @param monoid the accumulation {@link Monoid}
     * @return the accumulation with the value
     */
    public Tuple2<A, W> runWriter(Monoid<W> monoid) {
        return delegate.<Identity<Tuple2<A, W>>>runWriterT(monoid).runIdentity();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Writer<W, Tuple2<A, B>> listens(Fn1<? super W, ? extends B> fn) {
        return new Writer<>(delegate.listens(fn));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Writer<W, A> censor(Fn1<? super W, ? extends W> fn) {
        return new Writer<>(delegate.censor(fn));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Writer<W, B> trampolineM(Fn1<? super A, ? extends MonadRec<RecursiveResult<A, B>, Writer<W, ?>>> fn) {
        return new Writer<>(delegate.trampolineM(a -> fn.apply(a).<Writer<W, RecursiveResult<A, B>>>coerce().delegate));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Writer<W, B> flatMap(Fn1<? super A, ? extends Monad<B, Writer<W, ?>>> f) {
        return new Writer<>(delegate.flatMap(f.fmap(writer -> writer.<Writer<W, B>>coerce().delegate)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Writer<W, B> pure(B b) {
        return listen(b);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Writer<W, B> fmap(Fn1<? super A, ? extends B> fn) {
        return MonadRec.super.<B>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Writer<W, B> zip(Applicative<Fn1<? super A, ? extends B>, Writer<W, ?>> appFn) {
        return MonadRec.super.zip(appFn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Lazy<Writer<W, B>> lazyZip(
            Lazy<? extends Applicative<Fn1<? super A, ? extends B>, Writer<W, ?>>> lazyAppFn) {
        return MonadRec.super.lazyZip(lazyAppFn).fmap(MonadRec<B, Writer<W, ?>>::coerce);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Writer<W, B> discardL(Applicative<B, Writer<W, ?>> appB) {
        return MonadRec.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Writer<W, A> discardR(Applicative<B, Writer<W, ?>> appB) {
        return MonadRec.super.discardR(appB).coerce();
    }

    /**
     * Construct a {@link Writer} from an accumulation.
     *
     * @param w   the accumulation
     * @param <W> the accumulation type
     * @return the {@link Writer}
     */
    public static <W> Writer<W, Unit> tell(W w) {
        return new Writer<>(WriterT.tell(new Identity<>(w)));
    }

    /**
     * Construct a {@link Writer} from a value.
     *
     * @param a   the output value
     * @param <W> the accumulation type
     * @param <A> the value type
     * @return the {@link Writer}
     */
    public static <W, A> Writer<W, A> listen(A a) {
        return new Writer<>(WriterT.listen(new Identity<>(a)));
    }

    /**
     * Construct a {@link Writer} from an accumulation and a value.
     *
     * @param aw  the output value and accumulation
     * @param <W> the accumulation type
     * @param <A> the value type
     * @return the {@link Writer}
     */
    public static <W, A> Writer<W, A> writer(Tuple2<A, W> aw) {
        return new Writer<>(writerT(new Identity<>(aw)));
    }

    /**
     * The canonical {@link Pure} instance for {@link Writer}.
     *
     * @param <W> the accumulation type
     * @return the {@link Pure} instance
     */
    public static <W> Pure<Writer<W, ?>> pureWriter() {
        Pure<WriterT<W, Identity<?>, ?>> pureWriterT = pureWriterT(pureIdentity());
        return new Pure<Writer<W, ?>>() {
            @Override
            public <A> Writer<W, A> checkedApply(A a) {
                return new Writer<>(pureWriterT.<A, WriterT<W, Identity<?>, A>>apply(a));
            }
        };
    }

    /**
     * Create a {@link Writer} from a delegate {@link WriterT} with an {@link Identity} embedding.
     *
     * @param writerT the delegate {@link WriterT}
     * @param <W>     the accumulation type
     * @param <A>     the value type
     * @return the {@link Writer}
     */
    public static <W, A> Writer<W, A> writer(WriterT<W, Identity<?>, A> writerT) {
        return new Writer<>(writerT);
    }
}
