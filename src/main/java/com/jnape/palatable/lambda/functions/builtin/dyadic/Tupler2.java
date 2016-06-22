package com.jnape.palatable.lambda.functions.builtin.dyadic;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.DyadicFunction;
import com.jnape.palatable.lambda.functions.MonadicFunction;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;

/**
 * Creates a <code>Tuple2</code> from two values.
 *
 * @param <A> The type of the first value; also the first slot type of returned Tuple2
 * @param <B> The type of the second value; also the second slot type of returned Tuple2
 * @see com.jnape.palatable.lambda.adt.hlist.Tuple2
 */
public final class Tupler2<A, B> implements DyadicFunction<A, B, Tuple2<A, B>> {

    private Tupler2() {
    }

    @Override
    public Tuple2<A, B> apply(A a, B b) {
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
