package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.MonadicFunction;
import com.jnape.palatable.lambda.iterators.ImmutableIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Filter {

    public static <A> Iterable<A> filter(final MonadicFunction<? super A, Boolean> predicate, final Iterable<A> as) {
        return new Iterable<A>() {
            @Override
            public Iterator<A> iterator() {
                final Iterator<A> asIterator = as.iterator();
                return new ImmutableIterator<A>() {
                    private A queued;

                    @Override
                    public boolean hasNext() {
                        return nextElementIsAlreadyQueued() || queuedAnotherElement();
                    }

                    @Override
                    public A next() {
                        if (hasNext())
                            return dequeueNextElement();

                        throw new NoSuchElementException();
                    }

                    private boolean nextElementIsAlreadyQueued() {
                        return queued != null;
                    }

                    private boolean queuedAnotherElement() {
                        while (asIterator.hasNext()) {
                            A next = asIterator.next();
                            if (predicate.apply(next)) {
                                queued = next;
                                return true;
                            }
                        }
                        return false;
                    }

                    private A dequeueNextElement() {
                        A swap = queued;
                        queued = null;
                        return swap;
                    }
                };
            }
        };
    }
}
