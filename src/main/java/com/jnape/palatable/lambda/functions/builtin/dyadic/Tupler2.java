package com.jnape.palatable.lambda.functions.builtin.dyadic;

import com.jnape.palatable.lambda.adt.tuples.Tuple2;
import com.jnape.palatable.lambda.functions.DyadicFunction;

import static com.jnape.palatable.lambda.adt.tuples.Tuple2.tuple;

public final class Tupler2<A, B> implements DyadicFunction<A, B, Tuple2<A, B>> {

    @Override
    public final Tuple2<A, B> apply(A a, B b) {
        return tuple(a, b);
    }

    public static <A, B> Tupler2<A, B> tupler() {
        return new Tupler2<>();
    }
}
