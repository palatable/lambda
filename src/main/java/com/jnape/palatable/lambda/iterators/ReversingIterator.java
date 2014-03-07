package com.jnape.palatable.lambda.iterators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

public class ReversingIterator<A> extends ImmutableIterator<A> {
    private final Iterator<A>     as;
    private final ListIterator<A> reversingIterator;

    public ReversingIterator(Iterator<A> as) {
        this.as = as;
        reversingIterator = new ArrayList<A>().listIterator();
    }

    @Override
    public boolean hasNext() {
        return readyToReverse() ? reversingIterator.hasPrevious() : as.hasNext();
    }

    @Override
    public A next() {
        if (!readyToReverse())
            prepareForReversal();

        return reversingIterator.previous();
    }

    private void prepareForReversal() {
        while (as.hasNext())
            reversingIterator.add(as.next());
    }

    private boolean readyToReverse() {
        return reversingIterator.hasNext() || reversingIterator.hasPrevious();
    }
}
