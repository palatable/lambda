package com.jnape.palatable.lambda.functions.builtin.dyadic;

import com.jnape.palatable.lambda.functions.DyadicFunction;
import com.jnape.palatable.lambda.functions.MonadicFunction;

public final class Partial2<A, B, C> implements DyadicFunction<DyadicFunction<A, B, C>, A, MonadicFunction<B, C>> {

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

    public static <A, B, C> MonadicFunction<B, C> partial2(DyadicFunction<A, B, C> function, final A a) {
        return partial2(function).apply(a);
    }
}
