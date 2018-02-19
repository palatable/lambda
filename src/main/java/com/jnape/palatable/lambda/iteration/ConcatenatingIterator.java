package com.jnape.palatable.lambda.iteration;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class ConcatenatingIterator<A> implements Iterator<A> {

    private final Supplier<Iterator<A>>        xsSupplier;
    private final Supplier<Iterator<A>>        ysSupplier;
    private final AtomicReference<Iterator<A>> xsRef;
    private final AtomicReference<Iterator<A>> ysRef;
    private       boolean                      iteratedXs;

    public ConcatenatingIterator(Iterable<A> xs, Iterable<A> ys) {
        xsSupplier = xs::iterator;
        ysSupplier = ys::iterator;
        xsRef = new AtomicReference<>();
        ysRef = new AtomicReference<>();
        iteratedXs = false;
    }

    @Override
    public boolean hasNext() {
        if (hasNext(xsRef, xsSupplier))
            return true;

        iteratedXs = true;
        return hasNext(ysRef, ysSupplier);
    }

    private boolean hasNext(AtomicReference<Iterator<A>> ref, Supplier<Iterator<A>> supplier) {
        Iterator<A> as = ref.updateAndGet(it -> it == null ? supplier.get() : it);
        while (as instanceof ConcatenatingIterator && ((ConcatenatingIterator<A>) as).iteratedXs)
            as = ref.updateAndGet(it -> ((ConcatenatingIterator<A>) it).ysRef.get());
        return as.hasNext();
    }

    @Override
    public A next() {
        if (!hasNext())
            throw new NoSuchElementException();

        return !iteratedXs ? xsRef.get().next() : ysRef.get().next();
    }
}
