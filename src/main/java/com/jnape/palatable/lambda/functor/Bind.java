package com.jnape.palatable.lambda.functor;

import java.util.function.Function;

public interface Bind<A, Bnd extends Bind> extends Apply<A, Bnd> {

    /**
     * Chain dependent computations that may continue or short-circuit based on previous results.
     *
     * @param f   the dependent computation over A
     * @param <B> the resulting {@link Bind} parameter type
     * @return the new {@link Bind} instance
     */
    <B> Bind<B, Bnd> flatMap(Function<? super A, ? extends Bind<B, Bnd>> f);

    @Override
    <B> Bind<B, Bnd> fmap(Function<? super A, ? extends B> fn);

    @Override
    default <B> Bind<B, Bnd> zip(Apply<Function<? super A, ? extends B>, Bnd> appFn) {
        return appFn.<Bind<Function<? super A, ? extends B>, Bnd>>coerce().flatMap(ab -> fmap(ab::apply));
    }
}
