package com.jnape.palatable.lambda.internal.iteration;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class RewindableIterator<A> extends ImmutableIterator<A> {

    private final Iterator<A> asIterator;
    private final Cache<A>    cache;
    private       boolean     rewound;

    public RewindableIterator(Iterator<A> asIterator) {
        this.asIterator = asIterator;
        cache = new Cache<>();
        rewound = false;
    }

    @Override
    public boolean hasNext() {
        return rewound ? cache.isNotEmpty() : asIterator.hasNext();
    }

    @Override
    public A next() {
        if (rewound) {
            rewound = false;
            return cache.retrieve();
        }
        A next = asIterator.next();
        cache.store(next);
        return next;
    }

    public void rewind() {
        if (cache.isEmpty())
            throw new NoSuchElementException();

        rewound = true;
    }

    public boolean isRewound() {
        return rewound;
    }

    private static class Cache<A> {

        private A cache;

        public void store(A a) {
            cache = a;
        }

        public A retrieve() {
            if (cache == null)
                throw new NoSuchElementException("Cache is empty.");

            return this.cache;
        }

        public boolean isEmpty() {
            return cache == null;
        }

        public boolean isNotEmpty() {
            return !isEmpty();
        }
    }
}
