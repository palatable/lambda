package com.jnape.palatable.lambda.iteration;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class SnocIterator<A> implements Iterator<A> {

    private final Iterator<A> as;
    private final Iterator<A> snocs;

    public SnocIterator(Iterator<A> as, Iterator<A> snocs) {
        this.as = as;
        this.snocs = snocs;
    }

    @Override
    public boolean hasNext() {
        return as.hasNext() || snocs.hasNext();
    }

    @Override
    public A next() {
        if (!hasNext())
            throw new NoSuchElementException();

        return as.hasNext() ? as.next() : snocs.next();
    }
}
