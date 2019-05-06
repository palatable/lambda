package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.functions.Fn1;

/**
 * Covariantly cast a value of type <code>B</code> to a value of subtype <code>A</code>. Unsafe.
 *
 * @param <A> the subtype
 * @param <B> the supertype
 */
public final class Downcast<A extends B, B> implements Fn1<B, A> {

    private static final Downcast<?, ?> INSTANCE = new Downcast<>();

    private Downcast() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public A checkedApply(B b) {
        return (A) b;
    }

    @SuppressWarnings("unchecked")
    public static <A extends B, B> Downcast<A, B> downcast() {
        return (Downcast<A, B>) INSTANCE;
    }

    public static <A extends B, B> A downcast(B b) {
        return Downcast.<A, B>downcast().apply(b);
    }
}
