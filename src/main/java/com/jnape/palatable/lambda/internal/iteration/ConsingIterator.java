package com.jnape.palatable.lambda.internal.iteration;

import com.jnape.palatable.lambda.functions.Fn0;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class ConsingIterator<A> implements Iterator<A> {

    private final A                head;
    private final Fn0<Iterator<A>> asSupplier;
    private       Iterator<A>      asIterator;
    private       boolean          iteratedHead;

    public ConsingIterator(A head, Iterable<A> as) {
        this.head = head;
        this.asSupplier = as::iterator;
        iteratedHead = false;
    }

    @Override
    public boolean hasNext() {
        if (!iteratedHead)
            return true;

        if (asIterator == null)
            asIterator = asSupplier.apply();

        return asIterator.hasNext();
    }

    @Override
    public A next() {
        if (!hasNext())
            throw new NoSuchElementException();

        if (!iteratedHead) {
            iteratedHead = true;
            return head;
        }

        while (asIterator instanceof ConsingIterator && ((ConsingIterator) asIterator).iteratedHead) {
            ConsingIterator<A> cons = (ConsingIterator<A>) asIterator;
            if (cons.iteratedHead && cons.asIterator != null)
                asIterator = cons.asIterator;
        }

        return asIterator.next();
    }
}
