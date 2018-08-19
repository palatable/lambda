package com.jnape.palatable.lambda.iteration;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

public final class PredicatedDroppingIterator<A> extends ImmutableIterator<A> {
    private final Function<? super A, ? extends Boolean> predicate;
    private final RewindableIterator<A>                  rewindableIterator;
    private       boolean                                finishedDropping;

    public PredicatedDroppingIterator(Function<? super A, ? extends Boolean> predicate, Iterator<A> asIterator) {
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
