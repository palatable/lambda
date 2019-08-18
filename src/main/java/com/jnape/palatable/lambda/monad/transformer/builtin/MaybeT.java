package com.jnape.palatable.lambda.monad.transformer.builtin;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.Lift;
import com.jnape.palatable.lambda.functions.specialized.Pure;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.builtin.Compose;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.monad.transformer.MonadT;

import java.util.Objects;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;

/**
 * A {@link MonadT monad transformer} for {@link Maybe}.
 *
 * @param <M> the outer {@link Monad}
 * @param <A> the carrier type
 */
public final class MaybeT<M extends Monad<?, M>, A> implements MonadT<M, A, MaybeT<M, ?>, MaybeT<?, ?>> {

    private final Monad<Maybe<A>, M> mma;

    private MaybeT(Monad<Maybe<A>, M> mma) {
        this.mma = mma;
    }

    /**
     * Recover the full structure of the embedded {@link Monad}.
     *
     * @param <MMA> the witnessed target type
     * @return the embedded {@link Monad}
     */
    public <MMA extends Monad<Maybe<A>, M>> MMA runMaybeT() {
        return mma.coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B, N extends Monad<?, N>> MaybeT<N, B> lift(Monad<B, N> mb) {
        return liftMaybeT().apply(mb);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> MaybeT<M, B> fmap(Fn1<? super A, ? extends B> fn) {
        return MonadT.super.<B>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> MaybeT<M, B> pure(B b) {
        return maybeT(mma.pure(just(b)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> MaybeT<M, B> zip(Applicative<Fn1<? super A, ? extends B>, MaybeT<M, ?>> appFn) {
        return MonadT.super.zip(appFn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Lazy<MaybeT<M, B>> lazyZip(
            Lazy<? extends Applicative<Fn1<? super A, ? extends B>, MaybeT<M, ?>>> lazyAppFn) {
        return new Compose<>(mma)
                .lazyZip(lazyAppFn.fmap(maybeT -> new Compose<>(
                        maybeT.<MaybeT<M, Fn1<? super A, ? extends B>>>coerce()
                                .<Monad<Maybe<Fn1<? super A, ? extends B>>, M>>runMaybeT())))
                .fmap(compose -> maybeT(compose.getCompose()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> MaybeT<M, B> flatMap(Fn1<? super A, ? extends Monad<B, MaybeT<M, ?>>> f) {
        return maybeT(mma.flatMap(ma -> ma
                .match(constantly(mma.pure(nothing())),
                       a -> f.apply(a).<MaybeT<M, B>>coerce().runMaybeT())));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> MaybeT<M, B> discardL(Applicative<B, MaybeT<M, ?>> appB) {
        return MonadT.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> MaybeT<M, A> discardR(Applicative<B, MaybeT<M, ?>> appB) {
        return MonadT.super.discardR(appB).coerce();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof MaybeT<?, ?> && Objects.equals(mma, ((MaybeT) other).mma);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mma);
    }

    @Override
    public String toString() {
        return "MaybeT{" +
                "mma=" + mma +
                '}';
    }

    /**
     * Static factory method for lifting a <code>{@link Monad}&lt;{@link Maybe}&lt;A&gt;, M&gt;</code> into a
     * {@link MaybeT}.
     *
     * @param mma the {@link Monad}&lt;{@link Maybe}&lt;A&gt;, M&gt;
     * @param <M> the outer {@link Monad} unification parameter
     * @param <A> the carrier type
     * @return the {@link MaybeT}
     */
    public static <M extends Monad<?, M>, A> MaybeT<M, A> maybeT(Monad<Maybe<A>, M> mma) {
        return new MaybeT<>(mma);
    }

    /**
     * The canonical {@link Pure} instance for {@link MaybeT}.
     *
     * @param pureM the argument {@link Monad} {@link Pure}
     * @param <M>   the argument {@link Monad} witness
     * @return the {@link Pure} instance
     */
    public static <M extends Monad<?, M>> Pure<MaybeT<M, ?>> pureMaybeT(Pure<M> pureM) {
        return new Pure<MaybeT<M, ?>>() {
            @Override
            public <A> MaybeT<M, A> checkedApply(A a) {
                return maybeT(pureM.<A, Monad<A, M>>apply(a).fmap(Maybe::just));
            }
        };
    }

    /**
     * {@link Lift} for {@link MaybeT}.
     * s
     *
     * @return the {@link Monad} lifted into {@link MaybeT}
     */
    public static Lift<MaybeT<?, ?>> liftMaybeT() {
        return new Lift<MaybeT<?, ?>>() {
            @Override
            public <A, M extends Monad<?, M>> MaybeT<M, A> checkedApply(Monad<A, M> ga) {
                return maybeT(ga.fmap(Maybe::just));
            }
        };
    }
}
