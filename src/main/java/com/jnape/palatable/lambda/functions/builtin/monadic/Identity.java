package com.jnape.palatable.lambda.functions.builtin.monadic;

import com.jnape.palatable.lambda.functions.MonadicFunction;

public final class Identity<A> implements MonadicFunction<A, A> {

    private static final Identity IDENTITY = new Identity();

    @Override
    public final A apply(A a) {
        return a;
    }

    @SuppressWarnings("unchecked")
    public static <A> Identity<A> id() {
        return (Identity<A>) IDENTITY;
    }
}
