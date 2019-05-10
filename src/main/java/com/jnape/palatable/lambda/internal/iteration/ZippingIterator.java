package com.jnape.palatable.lambda.internal.iteration;

import com.jnape.palatable.lambda.functions.Fn2;

import java.util.Iterator;

public final class ZippingIterator<C, A, B> extends ImmutableIterator<C> {
    private final Fn2<? super A, ? super B, ? extends C> zipper;
    private final Iterator<A>                            asIterator;
    private final Iterator<B>                            bsIterator;

    public ZippingIterator(Fn2<? super A, ? super B, ? extends C> zipper, Iterator<A> asIterator,
                           Iterator<B> bsIterator) {
        this.asIterator = asIterator;
        this.bsIterator = bsIterator;
        this.zipper = zipper;
    }

    @Override
    public boolean hasNext() {
        return asIterator.hasNext() && bsIterator.hasNext();
    }

    @Override
    public C next() {
        return zipper.apply(asIterator.next(), bsIterator.next());
    }
}
