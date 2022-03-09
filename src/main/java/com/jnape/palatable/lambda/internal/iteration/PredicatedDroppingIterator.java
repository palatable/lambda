package com.jnape.palatable.lambda.internal.iteration;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.internal.ImmutableQueue;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class PredicatedDroppingIterator<A> extends ImmutableIterator<A> {
    private final Iterator<Fn1<? super A, ? extends Boolean>> predicates;
    private final RewindableIterator<A> rewindableIterator;

    public PredicatedDroppingIterator(ImmutableQueue<Fn1<? super A, ? extends Boolean>> predicates, Iterator<A> asIterator) {
        this.predicates = predicates.iterator();
        rewindableIterator = new RewindableIterator<>(asIterator);
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

        while (predicates.hasNext() && rewindableIterator.hasNext()) {
            Fn1<? super A, ? extends Boolean> predicate = predicates.next();
            boolean predicateDone = false;

            while (rewindableIterator.hasNext() && !predicateDone) {
                A next = rewindableIterator.next();
                Boolean apply = predicate.apply(next);
                if (!apply) {
                    rewindableIterator.rewind();
                    predicateDone = true;
                }
            }

        }
    }
}
