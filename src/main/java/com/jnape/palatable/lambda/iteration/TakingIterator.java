package com.jnape.palatable.lambda.iteration;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class TakingIterator<A> extends ImmutableIterator<A> {

    private final int         n;
    private final Iterator<A> iterator;
    private       int         currentIndex;

    public TakingIterator(int n, Iterator<A> iterator) {
        this.n = n;
        this.iterator = iterator;
        currentIndex = 0;
    }

    @Override
    public boolean hasNext() {
        return currentIndex < n && iterator.hasNext();
    }

    @Override
    public A next() {
        if (currentIndex >= n)
            throw new NoSuchElementException();

        currentIndex++;
        return iterator.next();
    }
}
