package com.jnape.palatable.lambda.iterators;

import com.jnape.palatable.lambda.MonadicFunction;

public class UnfoldingIterator<A> extends InfiniteIterator<A> {
    private final MonadicFunction<? super A, ? extends A> fn;
    private       A                                       acc;

    public UnfoldingIterator(MonadicFunction<? super A, ? extends A> fn, A seed) {
        this.fn = fn;
        acc = seed;
    }

    @Override
    public A next() {
        A next = acc;
        acc = fn.apply(acc);
        return next;
    }
}
