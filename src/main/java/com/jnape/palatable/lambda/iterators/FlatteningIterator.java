package com.jnape.palatable.lambda.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class FlatteningIterator<A> extends ImmutableIterator<A> {
    private final Iterator<? extends Iterable<? extends A>> xss;
    private       Iterator<? extends A>                     xs;

    public FlatteningIterator(Iterator<? extends Iterable<? extends A>> xss) {
        this.xss = xss;
    }

    @Override
    public boolean hasNext() {
        while (xss.hasNext() && (xs == null || !xs.hasNext()))
            xs = xss.next().iterator();

        return xs != null && xs.hasNext();
    }

    @Override
    public A next() {
        if (!hasNext())
            throw new NoSuchElementException();

        return xs.next();
    }
}
