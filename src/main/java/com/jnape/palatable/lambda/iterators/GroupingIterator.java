package com.jnape.palatable.lambda.iterators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
        int i = 0;
        while (i++ < k && asIterator.hasNext())
            group.add(asIterator.next());
        return group;
    }
}
