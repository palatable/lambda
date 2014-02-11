package com.jnape.palatable.lambda.staticfactory;

import com.jnape.palatable.lambda.iterators.ImmutableIterator;

import java.util.Iterator;

import static java.util.Arrays.asList;

public class IterableFactory {

    public static <A> Iterable<A> iterable(final Iterator<A> iterator) {
        return new Iterable<A>() {
            @Override
            public Iterator<A> iterator() {
                return new ImmutableIterator<A>() {
                    @Override
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    @Override
                    public A next() {
                        return iterator.next();
                    }
                };
            }
        };
    }

    public static <A> Iterable<A> iterable(final A... as) {
        return asList(as);
    }
}
