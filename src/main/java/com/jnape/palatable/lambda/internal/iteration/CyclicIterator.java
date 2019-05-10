package com.jnape.palatable.lambda.internal.iteration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

public final class CyclicIterator<A> extends InfiniteIterator<A> {
    private final Iterator<A>     iterator;
    private final ListIterator<A> doublyLinkedIterator;

    public CyclicIterator(Iterator<A> iterator) {
        this.iterator = iterator;
        doublyLinkedIterator = new ArrayList<A>().listIterator();
    }

    @Override
    public A next() {
        return iterator.hasNext() ? continueInitialIteration() : continueCachedIteration();
    }

    private A continueCachedIteration() {
        if (!doublyLinkedIterator.hasNext())
            while (doublyLinkedIterator.hasPrevious())
                doublyLinkedIterator.previous();

        return doublyLinkedIterator.next();
    }

    private A continueInitialIteration() {
        A next = iterator.next();
        doublyLinkedIterator.add(next);
        return next;
    }
}
