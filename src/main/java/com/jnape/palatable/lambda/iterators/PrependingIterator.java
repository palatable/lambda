package com.jnape.palatable.lambda.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class PrependingIterator<A> extends ImmutableIterator<A> {

    private final A           antecedent;
    private final Iterator<A> iterator;
    private       boolean     prependNext;

    public PrependingIterator(A antecedent, Iterator<A> iterator) {
        this.antecedent = antecedent;
        this.iterator = iterator;
        prependNext = true;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public A next() {
        if (!iterator.hasNext())
            throw new NoSuchElementException();

        return (prependNext = !prependNext) ? iterator.next() : antecedent;
    }
}
