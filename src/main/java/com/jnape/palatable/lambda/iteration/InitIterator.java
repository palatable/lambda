package com.jnape.palatable.lambda.iteration;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class InitIterator<A> extends ImmutableIterator<A> {
    private final Iterator<A> asIterator;
    private       A           queued;

    public InitIterator(Iterable<A> as) {
        asIterator = as.iterator();
    }

    @Override
    public boolean hasNext() {
        if (queued == null)
            if (asIterator.hasNext())
                queued = asIterator.next();

        return asIterator.hasNext();
    }

    @Override
    public A next() {
        if (!hasNext())
            throw new NoSuchElementException();

        A next = queued;
        queued = asIterator.next();
        return next;
    }
}
