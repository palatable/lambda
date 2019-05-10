package com.jnape.palatable.lambda.internal.iteration;

import java.util.Iterator;

public final class DroppingIterable<A> implements Iterable<A> {
    private final int         n;
    private final Iterable<A> as;

    public DroppingIterable(int n, Iterable<A> as) {
        while (as instanceof DroppingIterable) {
            DroppingIterable<A> nested = (DroppingIterable<A>) as;
            as = nested.as;
            n += nested.n;
        }
        this.as = as;
        this.n = n;
    }

    @Override
    public Iterator<A> iterator() {
        return new DroppingIterator<>(n, as.iterator());
    }
}
