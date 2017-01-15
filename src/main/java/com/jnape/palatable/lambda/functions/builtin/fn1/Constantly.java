package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;

/**
 * A function that takes two arguments and always returns the first argument.
 *
 * @param <A> The first argument type, and the the function's return type
 * @param <B> The second (ignored) argument type
 */
public final class Constantly<A, B> implements Fn2<A, B, A> {

    private static final Constantly INSTANCE = new Constantly();

    private Constantly() {
    }

    @Override
    public A apply(A a, B b) {
        return a;
    }

    @SuppressWarnings("unchecked")
    public static <A, B> Constantly<A, B> constantly() {
        return INSTANCE;
    }

    public static <A, B> Fn1<B, A> constantly(A a) {
        return Constantly.<A, B>constantly().apply(a);
    }

    public static <A, B> A constantly(A a, B b) {
        return constantly(a).apply(b);
    }
}
