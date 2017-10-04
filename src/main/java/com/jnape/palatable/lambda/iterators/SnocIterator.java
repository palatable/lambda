package com.jnape.palatable.lambda.iterators;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;

public final class SnocIterator<A> implements Iterator<A> {

    private final Supplier<Iterator<A>> initsSupplier;
    private final A                     last;
    private       Iterator<A>           inits;
    private       Iterator<A>           lasts;

    public SnocIterator(A last, Iterable<A> inits) {
        this.last = last;
        initsSupplier = inits::iterator;
    }

    @Override
    public boolean hasNext() {
        if (inits == null)
            queueAndDeforest();

        return inits.hasNext() || lasts.hasNext();
    }

    @Override
    public A next() {
        if (!hasNext())
            throw new NoSuchElementException();

        return inits.hasNext() ? inits.next() : lasts.next();
    }

    private void queueAndDeforest() {
        Iterable<A> lastConses = Collections::emptyIterator;
        inits = this;
        while (inits instanceof SnocIterator) {
            SnocIterator<A> it = (SnocIterator<A>) inits;
            lastConses = cons(it.last, lastConses);
            inits = it.initsSupplier.get();
        }
        lasts = lastConses.iterator();
    }
}
