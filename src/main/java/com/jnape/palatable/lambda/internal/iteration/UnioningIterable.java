package com.jnape.palatable.lambda.internal.iteration;

import java.util.Iterator;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Distinct.distinct;

public final class UnioningIterable<A> implements Iterable<A> {

    private final ConcatenatingIterable<A> elements;

    public UnioningIterable(Iterable<A> xs, Iterable<A> ys) {
        elements = new ConcatenatingIterable<>(xs instanceof UnioningIterable ? ((UnioningIterable<A>) xs).elements : xs,
                                               ys instanceof UnioningIterable ? ((UnioningIterable<A>) ys).elements : ys);
    }

    @Override
    public Iterator<A> iterator() {
        return distinct(elements).iterator();
    }
}
