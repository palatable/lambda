package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.iterators.ImmutableIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InGroupsOf {

    public static <A> Iterable<Iterable<A>> inGroupsOf(final int k, final Iterable<A> as) {
        return new Iterable<Iterable<A>>() {
            @Override
            public Iterator<Iterable<A>> iterator() {
                final Iterator<A> asIterator = as.iterator();
                return new ImmutableIterator<Iterable<A>>() {
                    @Override
                    public boolean hasNext() {
                        return asIterator.hasNext();
                    }

                    @Override
                    public Iterable<A> next() {
                        List<A> nextGroup = new ArrayList<A>();
                        int i = 0;
                        while (asIterator.hasNext() && i++ < k)
                            nextGroup.add(asIterator.next());
                        return nextGroup;
                    }
                };
            }
        };
    }
}
