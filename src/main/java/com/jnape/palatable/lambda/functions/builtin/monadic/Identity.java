package com.jnape.palatable.lambda.functions.builtin.monadic;

import com.jnape.palatable.lambda.functions.MonadicFunction;

/**
 * The identity function.
 *
 * @param <A> The input/output type
 */
public final class Identity<A> implements MonadicFunction<A, A> {

    private static final Identity IDENTITY = new Identity();

    private Identity() {
    }

    @Override
    public A apply(A a) {
        return a;
    }

    @SuppressWarnings("unchecked")
    public static <A> Identity<A> id() {
        return (Identity<A>) IDENTITY;
    }
}
