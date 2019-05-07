package com.jnape.palatable.lambda.iteration;

import com.jnape.palatable.lambda.functions.Fn1;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class FilteringIterator<A> extends ImmutableIterator<A> {

    private final Fn1<? super A, ? extends Boolean> predicate;
    private final RewindableIterator<A>             rewindableIterator;

    public FilteringIterator(Fn1<? super A, ? extends Boolean> predicate, Iterator<A> iterator) {
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
