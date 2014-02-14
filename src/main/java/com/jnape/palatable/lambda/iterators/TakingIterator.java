package com.jnape.palatable.lambda.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class TakingIterator<A> extends ImmutableIterator<A> {
    private final int         n;
    private final Iterator<A> iterator;
    private       int         size;

    public TakingIterator(int n, Iterator<A> iterator) {
        this.n = n;
        this.iterator = iterator;
        size = 0;
    }

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
}
