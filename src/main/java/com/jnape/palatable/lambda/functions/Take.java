package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.iterators.TakingIterator;

import java.util.Iterator;

public class Take {

    public static <A> Iterable<A> take(final int n, final Iterable<A> iterable) {
        return new Iterable<A>() {
            @Override
            public Iterator<A> iterator() {
                return new TakingIterator<A>(n, iterable.iterator());
            }
        };
    }
}
