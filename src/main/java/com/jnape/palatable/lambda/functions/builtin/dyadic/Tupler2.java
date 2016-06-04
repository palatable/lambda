package com.jnape.palatable.lambda.functions.builtin.dyadic;

import com.jnape.palatable.lambda.adt.tuples.Tuple2;
import com.jnape.palatable.lambda.functions.DyadicFunction;
import com.jnape.palatable.lambda.functions.MonadicFunction;

import static com.jnape.palatable.lambda.adt.tuples.Tuple2.tuple;

public final class Tupler2<A, B> implements DyadicFunction<A, B, Tuple2<A, B>> {

    private Tupler2() {
    }

    @Override
    public final Tuple2<A, B> apply(A a, B b) {
        return tuple(a, b);
    }

    public static <A, B> Tupler2<A, B> tupler() {
        return new Tupler2<>();
    }

    public static <A, B> MonadicFunction<B, Tuple2<A, B>> tupler(A a) {
        return Tupler2.<A, B>tupler().apply(a);
    }

    public static <A, B> Tuple2<A, B> tupler(A a, B b) {
        return Tupler2.<A, B>tupler(a).apply(b);
    }
}
