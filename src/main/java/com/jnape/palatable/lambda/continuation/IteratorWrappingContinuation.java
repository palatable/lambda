package com.jnape.palatable.lambda.continuation;

import com.jnape.palatable.lambda.tuples.Tuple2;

import java.util.Iterator;
import java.util.Optional;

import static com.jnape.palatable.lambda.continuation.Memo.memoize;
import static com.jnape.palatable.lambda.tuples.Tuple2.tuple;

public final class IteratorWrappingContinuation<A> implements Continuation<A> {
    private final Memo<Optional<Tuple2<A, Continuation<A>>>> memoizedResult;

    public IteratorWrappingContinuation(Iterator<A> iterator) {
        memoizedResult = memoize(() -> (iterator.hasNext())
                ? Optional.of(tuple(iterator.next(), new IteratorWrappingContinuation<>(iterator)))
                : Optional.empty());
    }

    @Override
    public Optional<Tuple2<A, Continuation<A>>> next() {
        return memoizedResult.get();
    }
}
