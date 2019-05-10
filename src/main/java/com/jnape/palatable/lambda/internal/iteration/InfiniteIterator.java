package com.jnape.palatable.lambda.internal.iteration;

public abstract class InfiniteIterator<A> extends ImmutableIterator<A> {
    @Override
    public final boolean hasNext() {
        return true;
    }
}
