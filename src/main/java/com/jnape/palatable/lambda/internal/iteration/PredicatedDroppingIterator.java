package com.jnape.palatable.lambda.internal.iteration;

import com.jnape.palatable.lambda.functions.Fn1;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public final class PredicatedDroppingIterator<A> extends ImmutableIterator<A> {
    private final List<Fn1<? super A, ? extends Boolean>> predicates;
    private final RewindableIterator<A> rewindableIterator;
    private boolean finishedDropping;

    public PredicatedDroppingIterator(List<Fn1<? super A, ? extends Boolean>> predicates, Iterator<A> asIterator) {
        this.predicates = predicates;
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
        if(finishedDropping)
            return;

        for (Fn1<? super A, ? extends Boolean> predicate : predicates) {
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

        finishedDropping = true;
    }
}
