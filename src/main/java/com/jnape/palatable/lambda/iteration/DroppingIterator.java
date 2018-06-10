package com.jnape.palatable.lambda.iteration;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class DroppingIterator<A> extends ImmutableIterator<A> {
    private final Integer     n;
    private final Iterator<A> asIterator;
    private       boolean     dropped;

    public DroppingIterator(Integer n, Iterator<A> asIterator) {
        this.n = n;
        this.asIterator = asIterator;
        dropped = false;
    }

    @Override
    public boolean hasNext() {
        if (!dropped) {
            drop();
        }
        return asIterator.hasNext();
    }

    @Override
    public A next() {
        if (!hasNext())
            throw new NoSuchElementException();

        return asIterator.next();
    }

    private void drop() {
        int i = 0;
        while (i++ < n && asIterator.hasNext())
            asIterator.next();
        dropped = true;
    }
}
