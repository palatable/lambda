package com.jnape.palatable.lambda.internal.iteration;

import java.util.HashMap;
import java.util.Iterator;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Filter.filter;

public final class DistinctIterable<A> implements Iterable<A> {

    private final Iterable<A> as;

    public DistinctIterable(Iterable<A> as) {
        while (as instanceof DistinctIterable) {
            as = ((DistinctIterable<A>) as).as;
        }
        this.as = as;
    }

    @Override
    public Iterator<A> iterator() {
        HashMap<A, Boolean> known = new HashMap<>();
        return filter(a -> known.putIfAbsent(a, true) == null, as).iterator();
    }
}
