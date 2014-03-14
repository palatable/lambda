package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.DyadicFunction;
import com.jnape.palatable.lambda.MonadicFunction;
import com.jnape.palatable.lambda.tuples.Tuple2;

import static com.jnape.palatable.lambda.tuples.Tuple2.tuple;

public final class Partial2<A, B, C> extends DyadicFunction<MonadicFunction<Tuple2<A, B>, C>, A, MonadicFunction<B, C>> {

    @Override
    public final MonadicFunction<B, C> apply(final MonadicFunction<Tuple2<A, B>, C> function, final A a) {
        return new MonadicFunction<B, C>() {
            @Override
            public C apply(B b) {
                return function.apply(tuple(a, b));
            }
        };
    }

    public static <A, B, C> Partial2<MonadicFunction<Tuple2<A, B>, C>, A, MonadicFunction<B, C>> partial2() {
        return new Partial2<MonadicFunction<Tuple2<A, B>, C>, A, MonadicFunction<B, C>>();
    }

    public static <A, B, C> MonadicFunction<A, MonadicFunction<B, C>> partial2(
            MonadicFunction<Tuple2<A, B>, C> function) {
        return Partial2.<A, B, C>partial2().apply(new Partial2<A, B, C>(), function);
    }

    public static <A, B, C> MonadicFunction<B, C> partial2(final MonadicFunction<Tuple2<A, B>, C> function, final A a) {
        return partial2(function).apply(a);
    }
}
