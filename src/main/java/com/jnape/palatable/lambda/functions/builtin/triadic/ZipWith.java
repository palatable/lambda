package com.jnape.palatable.lambda.functions.builtin.triadic;

import com.jnape.palatable.lambda.functions.DyadicFunction;
import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.functions.TriadicFunction;
import com.jnape.palatable.lambda.iterators.ZippingIterator;

/**
 * Zip together two <code>Iterable</code>s by applying a zipping function to the successive elements of each
 * <code>Iterable</code> until one of them runs out of elements. Returns an <code>Iterable</code> containing the
 * results.
 *
 * @param <A> The first input Iterable element type
 * @param <B> The second input Iterable element type
 * @param <C> The output Iterable element type
 * @see com.jnape.palatable.lambda.functions.builtin.dyadic.Zip
 */
public final class ZipWith<A, B, C> implements TriadicFunction<DyadicFunction<? super A, ? super B, ? extends C>, Iterable<A>, Iterable<B>, Iterable<C>> {

    @Override
    public Iterable<C> apply(DyadicFunction<? super A, ? super B, ? extends C> zipper, Iterable<A> as, Iterable<B> bs) {
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
            DyadicFunction<? super A, ? super B, ? extends C> zipper, Iterable<A> as) {
        return ZipWith.<A, B, C>zipWith(zipper).apply(as);
    }

    public static <A, B, C> Iterable<C> zipWith(DyadicFunction<? super A, ? super B, ? extends C> zipper,
                                                Iterable<A> as, Iterable<B> bs) {
        return ZipWith.<A, B, C>zipWith(zipper, as).apply(bs);
    }
}
