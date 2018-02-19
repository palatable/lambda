package com.jnape.palatable.lambda.iteration;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class FilteringIterator<A> extends ImmutableIterator<A> {

    private final Function<? super A, Boolean> predicate;
    private final RewindableIterator<A>        rewindableIterator;

    public FilteringIterator(Function<? super A, Boolean> predicate, Iterator<A> iterator) {
        this.predicate = predicate;
        rewindableIterator = new RewindableIterator<>(iterator);
    }

    @Override
    public boolean hasNext() {
        return rewindableIterator.isRewound() || hasMoreMatchingElements();
    }

    @Override
    public A next() {
        if (hasNext())
            return rewindableIterator.next();

        throw new NoSuchElementException();
    }

    private boolean hasMoreMatchingElements() {
        while (rewindableIterator.hasNext()) {
            if (predicate.apply(rewindableIterator.next())) {
                rewindableIterator.rewind();
                return true;
            }
        }

        return false;
    }
}
