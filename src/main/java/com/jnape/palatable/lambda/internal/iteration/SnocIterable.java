package com.jnape.palatable.lambda.internal.iteration;

import java.util.Collections;
import java.util.Iterator;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;
import static com.jnape.palatable.lambda.monoid.builtin.Concat.concat;

public final class SnocIterable<A> implements Iterable<A> {
    private final Iterable<A> as;
    private final Iterable<A> snocs;

    public SnocIterable(A a, Iterable<A> as) {
        Iterable<A> snocs = cons(a, Collections::emptyIterator);
        while (as instanceof SnocIterable<A> nested) {
            as = nested.as;
            snocs = concat(nested.snocs, snocs);
        }
        this.as = as;
        this.snocs = snocs;
    }

    @Override
    public Iterator<A> iterator() {
        return new SnocIterator<>(as.iterator(), snocs.iterator());
    }
}
