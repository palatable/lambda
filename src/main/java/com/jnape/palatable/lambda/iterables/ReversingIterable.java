package com.jnape.palatable.lambda.iterables;

import com.jnape.palatable.lambda.iterators.ImmutableIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

public class ReversingIterable<A> implements Iterable<A> {

    private final Iterable<A> as;

    public ReversingIterable(Iterable<A> as) {
        this.as = as;
    }

    @Override
    public Iterator<A> iterator() {
        final Iterator<A> asIterator = as.iterator();

        return new ImmutableIterator<A>() {
            private ListIterator<A> reversingIterator = null;

            @Override
            public boolean hasNext() {
                return readyToReverse() ? reversingIterator.hasPrevious() : asIterator.hasNext();
            }

            @Override
            public A next() {
                if (!readyToReverse())
                    prepareForReversal();

                return reversingIterator.previous();
            }

            private void prepareForReversal() {
                ArrayList<A> asList = new ArrayList<A>() {{
                    while (asIterator.hasNext())
                        add(asIterator.next());
                }};
                reversingIterator = asList.listIterator(asList.size());
            }

            private boolean readyToReverse() {
                return reversingIterator != null;
            }
        };
    }

    public static <A> ReversingIterable<A> reverse(Iterable<A> as) {
        return new ReversingIterable<A>(as);
    }
}
