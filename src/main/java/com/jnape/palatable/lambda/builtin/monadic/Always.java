package com.jnape.palatable.lambda.builtin.monadic;

import com.jnape.palatable.lambda.MonadicFunction;

public final class Always<A, B> extends MonadicFunction<A, B> {

    private final B b;

    public Always(B b) {
        this.b = b;
    }

    @Override
    public final B apply(A a) {
        return b;
    }

    public static <A, B> MonadicFunction<A, B> always(B b) {
        return new Always<A, B>(b);
    }
}
