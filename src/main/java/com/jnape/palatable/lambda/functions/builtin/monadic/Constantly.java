package com.jnape.palatable.lambda.functions.builtin.monadic;

import com.jnape.palatable.lambda.functions.MonadicFunction;

/**
 * Given a value, produce a function that returns that value, regardless of input.
 *
 * @param <A> The input type of the resulting function
 * @param <B> The output type of the resulting function
 */
public final class Constantly<A, B> implements MonadicFunction<A, B> {

    private final B b;

    private Constantly(B b) {
        this.b = b;
    }

    @Override
    public B apply(A a) {
        return b;
    }

    public static <A, B> MonadicFunction<A, B> constantly(B b) {
        return new Constantly<>(b);
    }
}
