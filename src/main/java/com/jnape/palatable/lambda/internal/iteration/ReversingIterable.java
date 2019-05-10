package com.jnape.palatable.lambda.internal.iteration;

import java.util.Iterator;

public final class ReversingIterable<A> implements Iterable<A> {
    private final Iterable<A> as;
    private final boolean     reverse;

    public ReversingIterable(Iterable<A> as) {
        boolean reverse = true;
        while (as instanceof ReversingIterable) {
            ReversingIterable<A> nested = (ReversingIterable<A>) as;
            as = nested.as;
            reverse = !nested.reverse;
        }
        this.as = as;
        this.reverse = reverse;
    }

    @Override
    public Iterator<A> iterator() {
        return reverse ? new ReversingIterator<>(as.iterator()) : as.iterator();
    }
}
