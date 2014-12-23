package com.jnape.palatable.lambda.functions.builtin.monadic;

import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.iterators.RepetitiousIterator;

public final class Repeat<A> implements MonadicFunction<A, Iterable<A>> {

    @Override
    public final Iterable<A> apply(final A a) {
        return () -> new RepetitiousIterator<>(a);
    }

    public static <A> Repeat<A> repeat() {
        return new Repeat<>();
    }

    public static <A> Iterable<A> repeat(final A a) {
        return Repeat.<A>repeat().apply(a);
    }
}
