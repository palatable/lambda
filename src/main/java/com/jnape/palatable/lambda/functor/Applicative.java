package com.jnape.palatable.lambda.functor;

import java.util.function.Function;

/**
 * Generic interface for the applicative functor type, which supports a lifting operation <code>pure</code> and a
 * flattening application <code>zip</code>.
 *
 * @param <A>   The type of the parameter
 * @param <App> The unification parameter
 */
public interface Applicative<A, App extends Applicative> extends Functor<A, App> {

    /**
     * Lift the value <code>b</code> into this applicative functor.
     *
     * @param b   the value
     * @param <B> the type of the returned applicative's parameter
     * @return an instance of this applicative over b
     */
    <B> Applicative<B, App> pure(B b);

    /**
     * Given another instance of this applicative over a mapping function, "zip" the two instances together using
     * whatever application semantics the current applicative supports
     *
     * @param appFn the other applicative instance
     * @param <B>   the resulting applicative parameter type
     * @return the mapped applicative
     */
    <B> Applicative<B, App> zip(Applicative<Function<? super A, ? extends B>, App> appFn);

    @Override
    default <B> Applicative<B, App> fmap(Function<? super A, ? extends B> fn) {
        return zip(pure(fn));
    }

    /**
     * Convenience method for coercing this applicative instance into another concrete type. Unsafe.
     *
     * @param <Concrete> the concrete applicative instance to coerce this applicative to
     * @return the coerced applicative
     */
    @SuppressWarnings("unchecked")
    default <Concrete extends Applicative<A, App>> Concrete coerce() {
        return (Concrete) this;
    }
}
