package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.MonadicFunction;
import com.jnape.palatable.lambda.iterators.RepetitiousIterator;

import java.util.Iterator;

public class Repeat<A> extends MonadicFunction<A, Iterable<A>> {

    @Override
    public Iterable<A> apply(final A a) {
        return new Iterable<A>() {
            @Override
            public Iterator<A> iterator() {
                return new RepetitiousIterator<A>(a);
            }
        };
    }

    public static <A> Iterable<A> repeat(final A a) {
        return new Repeat<A>().apply(a);
    }
}
