package com.jnape.palatable.lambda.functions.builtin.monadic;

import com.jnape.palatable.lambda.functions.MonadicFunction;

public final class Always<A, B> implements MonadicFunction<A, B> {

    private final B b;

    public Always(B b) {
        this.b = b;
    }

    @Override
    public final B apply(A a) {
        return b;
    }

    public static <A, B> MonadicFunction<A, B> always(B b) {
        return new Always<>(b);
    }
}
