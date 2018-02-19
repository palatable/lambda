package com.jnape.palatable.lambda.iteration;

import java.util.Iterator;
import java.util.function.BiFunction;

public class ZippingIterator<C, A, B> extends ImmutableIterator<C> {
    private final BiFunction<? super A, ? super B, ? extends C> zipper;
    private final Iterator<A>                                   asIterator;
    private final Iterator<B>                                   bsIterator;

    public ZippingIterator(BiFunction<? super A, ? super B, ? extends C> zipper, Iterator<A> asIterator,
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
