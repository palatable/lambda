package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.internal.iteration.RepetitiousIterator;

/**
 * Given a value, return an infinite <code>Iterable</code> that repeatedly iterates that value.
 *
 * @param <A> The Iterable element type
 */
public final class Repeat<A> implements Fn1<A, Iterable<A>> {

    private static final Repeat<?> INSTANCE = new Repeat<>();

    private Repeat() {
    }

    @Override
    public Iterable<A> checkedApply(A a) {
        return () -> new RepetitiousIterator<>(a);
    }

    @SuppressWarnings("unchecked")
    public static <A> Repeat<A> repeat() {
        return (Repeat<A>) INSTANCE;
    }

    public static <A> Iterable<A> repeat(A a) {
        return Repeat.<A>repeat().apply(a);
    }
}
