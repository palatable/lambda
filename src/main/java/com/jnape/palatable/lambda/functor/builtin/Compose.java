package com.jnape.palatable.lambda.functor.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.Pure;
import com.jnape.palatable.lambda.functor.Applicative;

import java.util.Objects;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Upcast.upcast;

/**
 * A functor representing the type-level composition of two {@link Applicative} functors; useful for preserving nested
 * type-level transformations during traversal of a {@link com.jnape.palatable.lambda.traversable.Traversable}.
 *
 * @param <F> The outer applicative
 * @param <G> The inner applicative
 * @param <A> The carrier type
 */
public final class Compose<F extends Applicative<?, F>, G extends Applicative<?, G>, A> implements
        Applicative<A, Compose<F, G, ?>> {

    private final Applicative<? extends Applicative<A, G>, F> fga;

    public Compose(Applicative<? extends Applicative<A, G>, F> fga) {
        this.fga = fga;
    }

    @SuppressWarnings("RedundantTypeArguments")
    public <GA extends Applicative<A, G>, FGA extends Applicative<GA, F>> FGA getCompose() {
        return fga.<GA>fmap(Applicative<A, G>::coerce).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Compose<F, G, B> fmap(Fn1<? super A, ? extends B> fn) {
        return new Compose<>(fga.fmap(g -> g.fmap(fn)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Compose<F, G, B> pure(B b) {
        return new Compose<>(fga.fmap(g -> g.pure(b)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Compose<F, G, B> zip(Applicative<Fn1<? super A, ? extends B>, Compose<F, G, ?>> appFn) {
        return new Compose<>(fga.zip(appFn.<Compose<F, G, Fn1<? super A, ? extends B>>>coerce()
                                             .getCompose().fmap(gFn -> g -> g.zip(gFn))));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Lazy<Compose<F, G, B>> lazyZip(
            Lazy<? extends Applicative<Fn1<? super A, ? extends B>, Compose<F, G, ?>>> lazyAppFn) {
        @SuppressWarnings("RedundantTypeArguments")
        Lazy<Applicative<Applicative<Fn1<? super A, ? extends B>, G>, F>> lazyAppFnCoerced =
                lazyAppFn
                        .<Compose<F, G, Fn1<? super A, ? extends B>>>fmap(
                                Applicative<Fn1<? super A, ? extends B>, Compose<F, G, ?>>::coerce)
                        .fmap(Compose<F, G, Fn1<? super A, ? extends B>>::getCompose);

        return fga.<Applicative<A, G>>fmap(upcast())
                .<Applicative<B, G>>lazyZip(lazyAppFnCoerced.fmap(fgf -> fgf.fmap(gf -> ga -> ga.zip(gf))))
                .fmap(Compose::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Compose<F, G, B> discardL(Applicative<B, Compose<F, G, ?>> appB) {
        return Applicative.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Compose<F, G, A> discardR(Applicative<B, Compose<F, G, ?>> appB) {
        return Applicative.super.discardR(appB).coerce();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Compose && Objects.equals(fga, ((Compose) other).fga);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fga);
    }

    @Override
    public String toString() {
        return "Compose{fga=" + fga + '}';
    }

    /**
     * The canonical {@link Pure} instance for {@link Compose}.
     *
     * @param pureF the {@link Pure} constructor for the outer {@link Applicative}
     * @param pureG the {@link Pure} constructor for the inner {@link Applicative}
     * @param <F>   the outer {@link Applicative} type
     * @param <G>   the inner {@link Applicative} type
     * @return the {@link Pure} instance
     */
    public static <F extends Applicative<?, F>, G extends Applicative<?, G>> Pure<Compose<F, G, ?>> pureCompose(
            Pure<F> pureF, Pure<G> pureG) {
        return new Pure<Compose<F, G, ?>>() {
            @Override
            public <A> Compose<F, G, A> checkedApply(A a) throws Throwable {
                return new Compose<>(pureF.<Applicative<A, G>, Applicative<Applicative<A, G>, F>>apply(pureG.apply(a)));
            }
        };
    }
}
