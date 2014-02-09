package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.MonadicFunction;
import com.jnape.palatable.lambda.iterators.FilteringIterator;

import java.util.Iterator;

public class Filter {

    public static <A> Iterable<A> filter(final MonadicFunction<? super A, Boolean> predicate, final Iterable<A> as) {
        return new Iterable<A>() {
            @Override
            public Iterator<A> iterator() {
                return new FilteringIterator<A>(predicate, as.iterator());
            }
        };
    }
}
