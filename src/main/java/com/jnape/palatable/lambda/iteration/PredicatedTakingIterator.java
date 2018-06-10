package com.jnape.palatable.lambda.iteration;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

public final class PredicatedTakingIterator<A> extends ImmutableIterator<A> {
    private final Function<? super A, Boolean> predicate;
    private final RewindableIterator<A>        rewindableIterator;
    private       boolean                      stillTaking;

    public PredicatedTakingIterator(Function<? super A, Boolean> predicate,
                                    Iterator<A> asIterator) {
        this.predicate = predicate;
        rewindableIterator = new RewindableIterator<>(asIterator);
        stillTaking = true;
    }

    @Override
    public boolean hasNext() {
        return stillTaking && rewindableIterator.hasNext() && predicateSucceedsOnNextElement();
    }

    @Override
    public A next() {
        if (!hasNext())
            throw new NoSuchElementException();

        return rewindableIterator.next();
    }

    private boolean predicateSucceedsOnNextElement() {
        if (rewindableIterator.isRewound())
            return true;

        if (predicate.apply(rewindableIterator.next())) {
            rewindableIterator.rewind();
            return stillTaking = true;
        }

        return stillTaking = false;
    }
}
