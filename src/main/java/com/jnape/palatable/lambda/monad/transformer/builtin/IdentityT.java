package com.jnape.palatable.lambda.monad.transformer.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.recursion.RecursiveResult;
import com.jnape.palatable.lambda.functions.specialized.Lift;
import com.jnape.palatable.lambda.functions.specialized.Pure;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.builtin.Compose;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monad.transformer.MonadT;

import java.util.Objects;

/**
 * A {@link MonadT monad transformer} for {@link Identity}.
 *
 * @param <M> the outer {@link Monad stack-safe monad}
 * @param <A> the carrier type
 */
public final class IdentityT<M extends MonadRec<?, M>, A> implements
        MonadT<M, A, IdentityT<M, ?>, IdentityT<?, ?>> {

    private final MonadRec<Identity<A>, M> mia;

    private IdentityT(MonadRec<Identity<A>, M> mia) {
        this.mia = mia;
    }

    /**
     * Recover the full structure of the embedded {@link Monad}.
     *
     * @param <MIA> the witnessed target type
     * @return the embedded {@link Monad}
     */
    public <MIA extends MonadRec<Identity<A>, M>> MIA runIdentityT() {
        return mia.coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B, N extends MonadRec<?, N>> IdentityT<N, B> lift(MonadRec<B, N> mb) {
        return liftIdentityT().apply(mb);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> IdentityT<M, B> flatMap(Fn1<? super A, ? extends Monad<B, IdentityT<M, ?>>> f) {
        return identityT(mia.flatMap(identityA -> f.apply(identityA.runIdentity())
                .<IdentityT<M, B>>coerce()
                .runIdentityT()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> IdentityT<M, B> trampolineM(
            Fn1<? super A, ? extends MonadRec<RecursiveResult<A, B>, IdentityT<M, ?>>> fn) {
        return identityT(runIdentityT().fmap(Identity::runIdentity)
                                 .trampolineM(a -> fn.apply(a)
                                         .<IdentityT<M, RecursiveResult<A, B>>>coerce()
                                         .runIdentityT()
                                         .fmap(Identity::runIdentity))
                                 .fmap(Identity::new));
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
                                .<MonadRec<Identity<Fn1<? super A, ? extends B>>, M>>runIdentityT())))
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
    public static <M extends MonadRec<?, M>, A> IdentityT<M, A> identityT(MonadRec<Identity<A>, M> mia) {
        return new IdentityT<>(mia);
    }

    /**
     * The canonical {@link Pure} instance for {@link IdentityT}.
     *
     * @param pureM the argument {@link Monad} {@link Pure}
     * @param <M>   the argument {@link Monad} witness
     * @return the {@link Pure} instance
     */
    public static <M extends MonadRec<?, M>> Pure<IdentityT<M, ?>> pureIdentityT(Pure<M> pureM) {
        return new Pure<IdentityT<M, ?>>() {
            @Override
            public <A> IdentityT<M, A> checkedApply(A a) {
                return identityT(pureM.<A, MonadRec<A, M>>apply(a).fmap(Identity::new));
            }
        };
    }

    /**
     * {@link Lift} for {@link IdentityT}.
     *
     * @return the {@link Monad} lifted into {@link IdentityT}
     */
    public static Lift<IdentityT<?, ?>> liftIdentityT() {
        return new Lift<IdentityT<?, ?>>() {
            @Override
            public <A, M extends MonadRec<?, M>> IdentityT<M, A> checkedApply(MonadRec<A, M> ga) {
                return identityT(ga.fmap(Identity::new));
            }
        };
    }
}
