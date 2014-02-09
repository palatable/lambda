package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.iterators.ImmutableIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Take {

    public static <A> Iterable<A> take(final int n, final Iterable<A> iterable) {
        return new Iterable<A>() {
            @Override
            public Iterator<A> iterator() {
                final Iterator<A> iterator = iterable.iterator();
                return new ImmutableIterator<A>() {
                    private int size = 0;

                    @Override
                    public boolean hasNext() {
                        return size < n && iterator.hasNext();
                    }

                    @Override
                    public A next() {
                        if (size >= n)
                            throw new NoSuchElementException();

                        size++;
                        return iterator.next();
                    }
                };
            }
        };
    }
}
