package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;

/**
 * Creates a <code>Tuple2</code> from two values.
 *
 * @param <A> The type of the first value; also the first slot type of returned Tuple2
 * @param <B> The type of the second value; also the second slot type of returned Tuple2
 * @see com.jnape.palatable.lambda.adt.hlist.Tuple2
 */
public final class Tupler2<A, B> implements Fn2<A, B, Tuple2<A, B>> {

    private static final Tupler2<?, ?> INSTANCE = new Tupler2<>();

    private Tupler2() {
    }

    @Override
    public Tuple2<A, B> checkedApply(A a, B b) {
        return tuple(a, b);
    }

    @SuppressWarnings("unchecked")
    public static <A, B> Tupler2<A, B> tupler() {
        return (Tupler2<A, B>) INSTANCE;
    }

    public static <A, B> Fn1<B, Tuple2<A, B>> tupler(A a) {
        return Tupler2.<A, B>tupler().apply(a);
    }

    public static <A, B> Tuple2<A, B> tupler(A a, B b) {
        return Tupler2.<A, B>tupler(a).apply(b);
    }
}
