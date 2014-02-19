package com.jnape.palatable.lambda.iterators;

import com.jnape.palatable.lambda.MonadicFunction;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class PredicatedTakingIterator<A> extends ImmutableIterator<A> {
    private final MonadicFunction<? super A, Boolean> predicate;
    private final SingleValueCachingIterator<A>       cachingIterator;
    private       boolean                             stillTaking;

    public PredicatedTakingIterator(MonadicFunction<? super A, Boolean> predicate,
                                    Iterator<A> iterator) {
        this.predicate = predicate;
        cachingIterator = new SingleValueCachingIterator<A>(iterator);
        stillTaking = true;
    }

    @Override
    public boolean hasNext() {
        return stillTaking && cachingIterator.hasNext() && predicateSucceedsOnNextElement();
    }

    @Override
    public A next() {
        if (!cachingIterator.hasCachedElement() && !predicateSucceedsOnNextElement())
            throw new NoSuchElementException();

        return cachingIterator.lastElementCached();
    }

    private boolean predicateSucceedsOnNextElement() {
        A nextElementToCheck = cachingIterator.hasCachedElement()
                ? cachingIterator.lastElementCached()
                : cachingIterator.next();

        return stillTaking = predicate.apply(nextElementToCheck);
    }
}
