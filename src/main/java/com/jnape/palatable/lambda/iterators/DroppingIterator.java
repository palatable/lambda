package com.jnape.palatable.lambda.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class DroppingIterator<A> extends ImmutableIterator<A> {
    private Integer     n;
    private Iterator<A> asIterator;
    private boolean     dropped;

    public DroppingIterator(Integer n, Iterator<A> asIterator) {
        this.n = n;
        this.asIterator = asIterator;
        dropped = false;
    }

    @Override
    public boolean hasNext() {
        if (!dropped) {
            deforest();
            drop();
            dropped = true;
        }
        return asIterator.hasNext();
    }

    @Override
    public A next() {
        if (!hasNext())
            throw new NoSuchElementException();

        return asIterator.next();
    }

    private void deforest() {
        while (asIterator instanceof DroppingIterator) {
            n += ((DroppingIterator) this.asIterator).n;
            asIterator = ((DroppingIterator<A>) this.asIterator).asIterator;
        }
    }

    private void drop() {
        int i = 0;
        while (i++ < n && asIterator.hasNext())
            asIterator.next();
    }
}
