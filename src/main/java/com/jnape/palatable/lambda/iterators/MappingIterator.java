package com.jnape.palatable.lambda.iterators;

import com.jnape.palatable.lambda.functions.Fn1;

import java.util.Iterator;

public class MappingIterator<A, B> extends ImmutableIterator<B> {

    private final Fn1<? super A, ? extends B> function;
    private final Iterator<A>                 iterator;

    public MappingIterator(Fn1<? super A, ? extends B> function, Iterator<A> iterator) {
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
