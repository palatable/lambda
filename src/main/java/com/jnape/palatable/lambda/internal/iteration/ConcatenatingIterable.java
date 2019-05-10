package com.jnape.palatable.lambda.internal.iteration;

import java.util.Iterator;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Flatten.flatten;

public final class ConcatenatingIterable<A> implements Iterable<A> {

    private final ImmutableQueue<Iterable<A>> iterables;

    public ConcatenatingIterable(Iterable<A> xs, Iterable<A> ys) {
        if (xs instanceof ConcatenatingIterable) {
            ImmutableQueue<Iterable<A>> iterables = ((ConcatenatingIterable<A>) xs).iterables;
            this.iterables = ys instanceof ConcatenatingIterable
                             ? iterables.concat(((ConcatenatingIterable<A>) ys).iterables)
                             : iterables.pushBack(ys);
        } else {
            iterables = ys instanceof ConcatenatingIterable
                        ? ((ConcatenatingIterable<A>) ys).iterables.pushFront(xs)
                        : ImmutableQueue.<Iterable<A>>empty().pushFront(ys).pushFront(xs);
        }
    }

    @Override
    public Iterator<A> iterator() {
        return flatten(iterables).iterator();
    }
}
