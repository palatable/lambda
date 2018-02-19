package com.jnape.palatable.lambda.iteration;

import java.util.Iterator;

public abstract class ImmutableIterator<Element> implements Iterator<Element> {

    @Override
    public final void remove() {
        throw new UnsupportedOperationException("Iterator is immutable.");
    }
}
