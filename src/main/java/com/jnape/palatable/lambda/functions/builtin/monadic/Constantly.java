package com.jnape.palatable.lambda.functions.builtin.monadic;

import com.jnape.palatable.lambda.functions.DyadicFunction;
import com.jnape.palatable.lambda.functions.MonadicFunction;

/**
 * A function that takes two arguments and always returns the first argument.
 *
 * @param <A> The first argument type, and the the function's return type
 * @param <B> The second (ignored) argument type
 */
public final class Constantly<A, B> implements DyadicFunction<A, B, A> {

    private Constantly() {
    }

    @Override
    public A apply(A a, B b) {
        return a;
    }

    public static <A, B> Constantly<A, B> constantly() {
        return new Constantly<>();
    }

    public static <A, B> MonadicFunction<B, A> constantly(A a) {
        return Constantly.<A, B>constantly().apply(a);
    }

    public static <A, B> A constantly(A a, B b) {
        return constantly(a).apply(b);
    }
}
