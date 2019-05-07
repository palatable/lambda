package com.jnape.palatable.lambda.iteration;

import com.jnape.palatable.lambda.functions.Fn1;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class PredicatedDroppingIterator<A> extends ImmutableIterator<A> {
    private final Fn1<? super A, ? extends Boolean> predicate;
    private final RewindableIterator<A>             rewindableIterator;
    private       boolean                           finishedDropping;

    public PredicatedDroppingIterator(Fn1<? super A, ? extends Boolean> predicate, Iterator<A> asIterator) {
        this.predicate = predicate;
        rewindableIterator = new RewindableIterator<>(asIterator);
        finishedDropping = false;
    }

    @Override
    public boolean hasNext() {
        dropElementsIfNecessary();
        return rewindableIterator.hasNext();
    }

    @Override
    public A next() {
        if (hasNext())
            return rewindableIterator.next();

        throw new NoSuchElementException();
    }

    private void dropElementsIfNecessary() {
        while (rewindableIterator.hasNext() && !finishedDropping) {
            if (!predicate.apply(rewindableIterator.next())) {
                rewindableIterator.rewind();
                finishedDropping = true;
            }
        }
    }
}
