package com.jnape.palatable.lambda.monad.transformer.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.builtin.Compose;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.monad.transformer.MonadT;

import java.util.Objects;

/**
 * A {@link MonadT monad transformer} for {@link Identity}.
 *
 * @param <M> the outer {@link Monad}
 * @param <A> the carrier type
 */
public final class IdentityT<M extends Monad<?, M>, A> implements MonadT<M, Identity<?>, A, IdentityT<M, ?>> {

    private final Monad<Identity<A>, M> mia;

    private IdentityT(Monad<Identity<A>, M> mia) {
        this.mia = mia;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <GA extends Monad<A, Identity<?>>, FGA extends Monad<GA, M>> FGA run() {
        return mia.<GA>fmap(Identity::coerce).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> IdentityT<M, B> flatMap(Fn1<? super A, ? extends Monad<B, IdentityT<M, ?>>> f) {
        return identityT(mia.flatMap(identityA -> f.apply(identityA.runIdentity()).<IdentityT<M, B>>coerce().run()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> IdentityT<M, B> pure(B b) {
        return identityT(mia.pure(new Identity<>(b)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> IdentityT<M, B> fmap(Fn1<? super A, ? extends B> fn) {
        return MonadT.super.<B>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> IdentityT<M, B> zip(Applicative<Fn1<? super A, ? extends B>, IdentityT<M, ?>> appFn) {
        return MonadT.super.zip(appFn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Lazy<IdentityT<M, B>> lazyZip(
            Lazy<? extends Applicative<Fn1<? super A, ? extends B>, IdentityT<M, ?>>> lazyAppFn) {
        return new Compose<>(mia)
                .lazyZip(lazyAppFn.fmap(maybeT -> new Compose<>(
                        maybeT.<IdentityT<M, Fn1<? super A, ? extends B>>>coerce()
                                .<Identity<Fn1<? super A, ? extends B>>,
                                        Monad<Identity<Fn1<? super A, ? extends B>>, M>>run())))
                .fmap(compose -> identityT(compose.getCompose()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> IdentityT<M, B> discardL(Applicative<B, IdentityT<M, ?>> appB) {
        return MonadT.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> IdentityT<M, A> discardR(Applicative<B, IdentityT<M, ?>> appB) {
        return MonadT.super.discardR(appB).coerce();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof IdentityT<?, ?> && Objects.equals(mia, ((IdentityT<?, ?>) other).mia);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mia);
    }

    @Override
    public String toString() {
        return "IdentityT{mia=" + mia + '}';
    }

    /**
     * Static factory method for lifting a <code>{@link Monad}&lt;{@link Identity}&lt;A&gt;, M&gt;</code> into a
     * {@link IdentityT}.
     *
     * @param mia the {@link Monad}&lt;{@link Identity}&lt;A&gt;, M&gt;
     * @param <M> the outer {@link Monad} unification parameter
     * @param <A> the carrier type
     * @return the new {@link IdentityT}.
     */
    public static <M extends Monad<?, M>, A> IdentityT<M, A> identityT(Monad<Identity<A>, M> mia) {
        return new IdentityT<>(mia);
    }
}
