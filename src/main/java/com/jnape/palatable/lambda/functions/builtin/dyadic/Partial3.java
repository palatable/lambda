package com.jnape.palatable.lambda.functions.builtin.dyadic;

import com.jnape.palatable.lambda.functions.DyadicFunction;
import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.tuples.Tuple3;

import static com.jnape.palatable.lambda.tuples.Tuple3.tuple;

public final class Partial3<A, B, C, D> extends DyadicFunction<MonadicFunction<Tuple3<A, B, C>, D>, A, DyadicFunction<B, C, D>> {

    @Override
    public final DyadicFunction<B, C, D> apply(final MonadicFunction<Tuple3<A, B, C>, D> function, final A a) {
        return new DyadicFunction<B, C, D>() {
            @Override
            public D apply(B b, C c) {
                return function.apply(tuple(a, b, c));
            }
        };
    }

    public static <A, B, C, D> Partial3<A, B, C, D> partial3() {
        return new Partial3<A, B, C, D>();
    }

    public static <A, B, C, D> MonadicFunction<A, DyadicFunction<B, C, D>> partial3(
            MonadicFunction<Tuple3<A, B, C>, D> function) {
        return Partial3.<A, B, C, D>partial3().partial(function);
    }

    public static <A, B, C, D> DyadicFunction<B, C, D> partial3(MonadicFunction<Tuple3<A, B, C>, D> function, A a) {
        return partial3(function).apply(a);
    }
}
