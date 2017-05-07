package com.jnape.palatable.lambda.iterators;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicReference;

public class ConcatenatingIterator<A> implements Iterator<A> {

    private final Iterable<A>                  xs;
    private final Iterable<A>                  ys;
    private final AtomicReference<Iterator<A>> xsRef;
    private final AtomicReference<Iterator<A>> ysRef;
    private final Queue<Iterable<A>>           afterXs;
    private final Queue<Iterable<A>>           afterYs;

    public ConcatenatingIterator(Iterable<A> xs, Iterable<A> ys) {
        this.xs = xs;
        this.ys = ys;
        xsRef = new AtomicReference<>();
        ysRef = new AtomicReference<>();
        afterXs = new LinkedList<>();
        afterYs = new LinkedList<>();
    }

    @Override
    public boolean hasNext() {
        queueNext(xsRef, xs, afterXs);

        if (xsIterator().hasNext())
            return true;

        queueNext(ysRef, ys, afterYs);

        return ysIterator().hasNext();
    }

    @Override
    public A next() {
        if (!hasNext())
            throw new NoSuchElementException();

        return xsIterator().hasNext() ? xsIterator().next() : ysIterator().next();
    }

    private Iterator<A> xsIterator() {
        return xsRef.get();
    }

    private Iterator<A> ysIterator() {
        return ysRef.get();
    }

    private void queueNext(AtomicReference<Iterator<A>> iteratorRef, Iterable<A> iterable,
                           Queue<Iterable<A>> queued) {
        iteratorRef.updateAndGet(iterator -> {
            if (iterator == null)
                iterator = iterable.iterator();

            while (iterator instanceof ConcatenatingIterator && iterator.hasNext()) {
                ConcatenatingIterator<A> concatenatingXsIterator = (ConcatenatingIterator<A>) iterator;

                if (concatenatingXsIterator.xsIterator().hasNext()) {
                    queued.add(concatenatingXsIterator.ys);
                    iterator = concatenatingXsIterator.xsIterator();
                } else {
                    iterator = concatenatingXsIterator.ysIterator();
                }
            }

            while (!iterator.hasNext() && !queued.isEmpty()) {
                iterator = queued.poll().iterator();
            }

            return iterator;
        });
    }

}
