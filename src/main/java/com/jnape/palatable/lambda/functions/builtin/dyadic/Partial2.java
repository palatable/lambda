package com.jnape.palatable.lambda.functions.builtin.dyadic;

import com.jnape.palatable.lambda.functions.DyadicFunction;
import com.jnape.palatable.lambda.functions.MonadicFunction;

/**
 * Partially apply (fix) the first argument of a <code>DyadicFunction</code>, producing a <code>MonadicFunction</code>
 * that takes the remaining argument. This is isomorphic to calling {@link com.jnape.palatable.lambda.functions.DyadicFunction#apply(Object)}.
 *
 * @param <A> The type of the value to be supplied
 * @param <B> The input argument type of the resulting function
 * @param <C> The return type of the resulting function
 * @see Partial3
 */
public final class Partial2<A, B, C> implements DyadicFunction<DyadicFunction<A, B, C>, A, MonadicFunction<B, C>> {

    private Partial2() {
    }

    @Override
    public MonadicFunction<B, C> apply(DyadicFunction<A, B, C> function, A a) {
        return function.apply(a);
    }

    public static <A, B, C> Partial2<DyadicFunction<A, B, C>, A, MonadicFunction<B, C>> partial2() {
        return new Partial2<>();
    }

    public static <A, B, C> MonadicFunction<A, MonadicFunction<B, C>> partial2(DyadicFunction<A, B, C> function) {
        return Partial2.<A, B, C>partial2().apply(new Partial2<>(), function);
    }

    public static <A, B, C> MonadicFunction<B, C> partial2(DyadicFunction<A, B, C> function, A a) {
        return partial2(function).apply(a);
    }
}
