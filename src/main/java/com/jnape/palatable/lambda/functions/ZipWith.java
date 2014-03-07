package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.DyadicFunction;
import com.jnape.palatable.lambda.MonadicFunction;
import com.jnape.palatable.lambda.TriadicFunction;
import com.jnape.palatable.lambda.iterators.ZippingIterator;

import java.util.Iterator;

public final class ZipWith<A, B, C> extends TriadicFunction<DyadicFunction<? super A, ? super B, ? extends C>, Iterable<A>, Iterable<B>, Iterable<C>> {

    @Override
    public final Iterable<C> apply(final DyadicFunction<? super A, ? super B, ? extends C> zipper, final Iterable<A> as,
                                   final Iterable<B> bs) {
        return new Iterable<C>() {
            @Override
            public Iterator<C> iterator() {
                return new ZippingIterator<C, A, B>(zipper, as.iterator(), bs.iterator());
            }
        };
    }

    public static <A, B, C> TriadicFunction<DyadicFunction<? super A, ? super B, ? extends C>, Iterable<A>, Iterable<B>, Iterable<C>> zipWith() {
        return new ZipWith<A, B, C>();
    }

    public static <A, B, C> DyadicFunction<Iterable<A>, Iterable<B>, Iterable<C>> zipWith(
            DyadicFunction<? super A, ? super B, ? extends C> zipper) {
        return ZipWith.<A, B, C>zipWith().partial(zipper);
    }

    public static <A, B, C> MonadicFunction<Iterable<B>, Iterable<C>> zipWith(
            final DyadicFunction<? super A, ? super B, ? extends C> zipper,
            final Iterable<A> as) {
        return zipWith(zipper).partial(as);
    }

    public static <A, B, C> Iterable<C> zipWith(final DyadicFunction<? super A, ? super B, ? extends C> zipper,
                                                final Iterable<A> as,
                                                final Iterable<B> bs) {
        return zipWith(zipper, as).apply(bs);
    }
}
