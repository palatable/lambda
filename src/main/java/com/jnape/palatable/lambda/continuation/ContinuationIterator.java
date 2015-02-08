package com.jnape.palatable.lambda.continuation;

import com.jnape.palatable.lambda.tuples.Tuple2;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class ContinuationIterator<A> implements Iterator<A> {
    private Continuation<A> continuation;

    public ContinuationIterator(Continuation<A> continuation) {
        this.continuation = continuation;
    }

    @Override
    public boolean hasNext() {
        return continuation.next().isPresent();
    }

    @Override
    public A next() {
        if (!hasNext())
            throw new NoSuchElementException();

        Tuple2<A, Continuation<A>> continuationResult = continuation.next().get();
        continuation = continuationResult._2;
        return continuationResult._1;
    }

    public static <A> ContinuationIterator<A> wrap(Continuation<A> continuable) {
        return new ContinuationIterator<>(continuable);
    }
}
