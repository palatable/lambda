package com.jnape.palatable.lambda.internal.iteration;

import java.util.Iterator;

import static java.lang.Math.min;

public final class TakingIterable<A> implements Iterable<A> {
    private final int         n;
    private final Iterable<A> as;

    public TakingIterable(int n, Iterable<A> as) {
        while (as instanceof TakingIterable) {
            TakingIterable<A> nested = (TakingIterable<A>) as;
            n = min(n, nested.n);
            as = nested.as;
        }
        this.n = n;
        this.as = as;
    }

    @Override
    public Iterator<A> iterator() {
        return new TakingIterator<>(n, as.iterator());
    }
}
