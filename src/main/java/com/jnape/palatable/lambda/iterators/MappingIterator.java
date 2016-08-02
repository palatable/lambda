package com.jnape.palatable.lambda.iterators;

import java.util.Iterator;
import java.util.function.Function;

public class MappingIterator<A, B> extends ImmutableIterator<B> {

    private final Function<? super A, ? extends B> function;
    private final Iterator<A>                      iterator;

    public MappingIterator(Function<? super A, ? extends B> function, Iterator<A> iterator) {
        this.function = function;
        this.iterator = iterator;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public B next() {
        return function.apply(iterator.next());
    }
}
