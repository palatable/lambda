package com.jnape.palatable.lambda.monad.transformer.builtin;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
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
public final class MaybeT<M extends Monad<?, M>, A> implements MonadT<M, Maybe<?>, A, MaybeT<M, ?>> {

    private final Monad<Maybe<A>, M> mma;

    private MaybeT(Monad<Maybe<A>, M> mma) {
        this.mma = mma;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <GA extends Monad<A, Maybe<?>>, FGA extends Monad<GA, M>> FGA run() {
        return mma.<GA>fmap(Applicative::coerce).coerce();
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
                                .<Maybe<Fn1<? super A, ? extends B>>, Monad<Maybe<Fn1<? super A, ? extends B>>, M>>run())))
                .fmap(compose -> maybeT(compose.getCompose()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> MaybeT<M, B> flatMap(Fn1<? super A, ? extends Monad<B, MaybeT<M, ?>>> f) {
        return maybeT(mma.flatMap(ma -> ma
                .match(constantly(mma.pure(nothing())),
                       a -> f.apply(a).<MaybeT<M, B>>coerce().run())));
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
}
