package com.jnape.palatable.lambda.iterators;

import com.jnape.palatable.lambda.functions.MonadicFunction;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class FilteringIterator<A> extends ImmutableIterator<A> {

    private final MonadicFunction<? super A, Boolean> predicate;
    private final RewindableIterator<A>               rewindableIterator;

    public FilteringIterator(MonadicFunction<? super A, Boolean> predicate, Iterator<A> iterator) {
        this.predicate = predicate;
        rewindableIterator = new RewindableIterator<A>(iterator);
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
