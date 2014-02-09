package com.jnape.palatable.lambda.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SingleValueCachingIterator<A> extends ImmutableIterator<A> {

    private static class Cache<A> {

        private A cache;

        public void store(A a) {
            cache = a;
        }

        public A retrieve() {
            if (cache == null)
                throw new NoSuchElementException("Cache is empty.");

            A cache = this.cache;
            this.cache = null;
            return cache;
        }

        public boolean isNotEmpty() {
            return cache != null;
        }
    }

    private final Iterator<A> iterator;
    private final Cache<A>    cache;

    public SingleValueCachingIterator(Iterator<A> iterator) {
        this.iterator = iterator;
        cache = new Cache<A>();
    }

    public A lastElementCached() {
        return cache.retrieve();
    }

    public boolean hasCachedElement() {
        return cache.isNotEmpty();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public A next() {
        A next = iterator.next();
        cache.store(next);
        return next;
    }
}
