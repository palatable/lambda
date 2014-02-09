package com.jnape.palatable.lambda.iterators;

import com.jnape.palatable.lambda.MonadicFunction;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class FilteringIterator<A> extends ImmutableIterator<A> {

    private final MonadicFunction<? super A, Boolean> predicate;
    private final SingleValueCachingIterator<A>       cachingIterator;

    public FilteringIterator(MonadicFunction<? super A, Boolean> predicate, Iterator<A> iterator) {
        this.predicate = predicate;
        cachingIterator = new SingleValueCachingIterator<A>(iterator);
    }

    @Override
    public boolean hasNext() {
        return cachingIterator.hasCachedElement() || hasMoreMatchingElements();
    }

    @Override
    public A next() {
        if (hasNext())
            return cachingIterator.lastElementCached();

        throw new NoSuchElementException();
    }

    private boolean hasMoreMatchingElements() {
        while (cachingIterator.hasNext())
            if (predicate.apply(cachingIterator.next()))
                return true;

        return false;
    }
}
