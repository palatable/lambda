package com.jnape.palatable.lambda.iterators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.jnape.palatable.lambda.functions.Take.take;
import static com.jnape.palatable.lambda.staticfactory.IterableFactory.iterable;

public class GroupingIterator<A> extends ImmutableIterator<Iterable<A>> {
    private final Integer     k;
    private final Iterator<A> asIterator;

    public GroupingIterator(Integer k, Iterator<A> asIterator) {
        this.k = k;
        this.asIterator = asIterator;
    }

    @Override
    public boolean hasNext() {
        return asIterator.hasNext();
    }

    @Override
    public Iterable<A> next() {
        List<A> group = new ArrayList<A>();
        for (A a : take(k, iterable(asIterator)))
            group.add(a);
        return group;
    }
}
