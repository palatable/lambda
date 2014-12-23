package com.jnape.palatable.lambda.functions.builtin.triadic;

import com.jnape.palatable.lambda.functions.DyadicFunction;
import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.functions.TriadicFunction;
import com.jnape.palatable.lambda.iterators.ZippingIterator;

public final class ZipWith<A, B, C> implements TriadicFunction<DyadicFunction<? super A, ? super B, ? extends C>, Iterable<A>, Iterable<B>, Iterable<C>> {

    @Override
    public final Iterable<C> apply(final DyadicFunction<? super A, ? super B, ? extends C> zipper,
                                   final Iterable<A> as,
                                   final Iterable<B> bs) {
        return () -> new ZippingIterator<>(zipper, as.iterator(), bs.iterator());
    }

    public static <A, B, C> ZipWith<A, B, C> zipWith() {
        return new ZipWith<>();
    }

    public static <A, B, C> DyadicFunction<Iterable<A>, Iterable<B>, Iterable<C>> zipWith(
            DyadicFunction<? super A, ? super B, ? extends C> zipper) {
        return ZipWith.<A, B, C>zipWith().apply(zipper);
    }

    public static <A, B, C> MonadicFunction<Iterable<B>, Iterable<C>> zipWith(
            final DyadicFunction<? super A, ? super B, ? extends C> zipper,
            final Iterable<A> as) {
        return zipWith(zipper).apply(as);
    }

    public static <A, B, C> Iterable<C> zipWith(final DyadicFunction<? super A, ? super B, ? extends C> zipper,
                                                final Iterable<A> as,
                                                final Iterable<B> bs) {
        return zipWith(zipper, as).apply(bs);
    }
}
