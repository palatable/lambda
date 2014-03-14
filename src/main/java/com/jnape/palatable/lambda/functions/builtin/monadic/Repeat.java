package com.jnape.palatable.lambda.functions.builtin.monadic;

import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.iterators.RepetitiousIterator;

import java.util.Iterator;

public final class Repeat<A> extends MonadicFunction<A, Iterable<A>> {

    @Override
    public final Iterable<A> apply(final A a) {
        return new Iterable<A>() {
            @Override
            public Iterator<A> iterator() {
                return new RepetitiousIterator<A>(a);
            }
        };
    }

    public static <A> Repeat<A> repeat() {
        return new Repeat<A>();
    }

    public static <A> Iterable<A> repeat(final A a) {
        return Repeat.<A>repeat().apply(a);
    }
}
