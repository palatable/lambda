package com.jnape.palatable.lambda.functor.builtin;

import com.jnape.palatable.lambda.functor.Applicative;

import java.util.Objects;
import java.util.function.Function;

/**
 * A functor representing the type-level composition of two {@link Applicative} functors; useful for preserving nested
 * type-level transformations during traversal of a {@link com.jnape.palatable.lambda.traversable.Traversable}.
 *
 * @param <F> The outer applicative
 * @param <G> The inner applicative
 * @param <A> The carrier type
 */
public class Compose<F extends Applicative, G extends Applicative, A> implements Applicative<A, Compose<F, G, ?>> {

    private final Applicative<Applicative<A, G>, F> fga;

    public Compose(Applicative<Applicative<A, G>, F> fga) {
        this.fga = fga;
    }

    public Applicative<? extends Applicative<A, G>, F> getCompose() {
        return fga;
    }

    @Override
    public <B> Compose<F, G, B> fmap(Function<? super A, ? extends B> fn) {
        return new Compose<>(fga.fmap(g -> g.fmap(fn)));
    }

    @Override
    public <B> Compose<F, G, B> pure(B b) {
        return new Compose<>(fga.fmap(g -> g.pure(b)));
    }

    @Override
    public <B> Compose<F, G, B> zip(Applicative<Function<? super A, ? extends B>, Compose<F, G, ?>> appFn) {
        return new Compose<>(fga.zip(appFn.<Compose<F, G, Function<? super A, ? extends B>>>coerce().getCompose().fmap(gFn -> g -> g.zip(gFn))));
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Compose && Objects.equals(fga, ((Compose) other).fga);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fga);
    }
}
