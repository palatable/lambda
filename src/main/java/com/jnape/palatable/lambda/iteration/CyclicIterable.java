package com.jnape.palatable.lambda.iteration;

import java.util.Iterator;

public final class CyclicIterable<A> implements Iterable<A> {
    private final Iterable<A> as;

    public CyclicIterable(Iterable<A> as) {
        while (as instanceof CyclicIterable) {
            as = ((CyclicIterable<A>) as).as;
        }
        this.as = as;
    }

    @Override
    public Iterator<A> iterator() {
        return new CyclicIterator<>(as.iterator());
    }
}
