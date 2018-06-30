package com.jnape.palatable.lambda.functor;

import java.util.function.Function;

public interface Apply<A, App extends Apply> extends Functor<A, App> {

    @Override
    <B> Apply<B, App> fmap(Function<? super A, ? extends B> fn);

    /**
     * Given another instance of this {@link Apply} over a mapping function, "zip" the two instances together using
     * whatever application semantics the current {@link Apply} supports.
     *
     * @param appFn the other {@link Apply} instance
     * @param <B>   the resulting {@link Apply} parameter type
     * @return the mapped {@link Apply}
     */
    <B> Apply<B, App> zip(Apply<Function<? super A, ? extends B>, App> appFn);

    /**
     * Convenience method for coercing this {@link Apply} instance into another concrete type. Unsafe.
     *
     * @param <Concrete> the concrete {@link Apply} instance to coerce this {@link Apply} to
     * @return the coerced {@link Apply}
     */
    @SuppressWarnings("unchecked")
    default <Concrete extends Apply<A, App>> Concrete coerce() {
        return (Concrete) this;
    }
}
