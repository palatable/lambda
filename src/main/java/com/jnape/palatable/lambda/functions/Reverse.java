package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.iterators.ImmutableIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

public class Reverse {

    public static <A> Iterable<A> reverse(final Iterable<A> as) {
        final Iterator<A> asIterator = as.iterator();
        return new Iterable<A>() {
            @Override
            public Iterator<A> iterator() {
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
        };
    }

}
