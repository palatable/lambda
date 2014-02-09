package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.iterators.ImmutableIterator;

import java.util.Iterator;

import static com.jnape.palatable.lambda.functions.Take.take;
import static com.jnape.palatable.lambda.staticfactory.IterableFactory.iterable;

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
                        return take(k, iterable(asIterator));
                    }
                };
            }
        };
    }
}
