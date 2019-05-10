package com.jnape.palatable.lambda.functions.builtin.fn3;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.internal.iteration.ZippingIterator;

/**
 * Zip together two <code>Iterable</code>s by applying a zipping function to the successive elements of each
 * <code>Iterable</code> until one of them runs out of elements. Returns an <code>Iterable</code> containing the
 * results.
 *
 * @param <A> The first input Iterable element type
 * @param <B> The second input Iterable element type
 * @param <C> The output Iterable element type
 * @see com.jnape.palatable.lambda.functions.builtin.fn2.Zip
 */
public final class ZipWith<A, B, C> implements Fn3<Fn2<? super A, ? super B, ? extends C>, Iterable<A>, Iterable<B>, Iterable<C>> {

    private static final ZipWith<?, ?, ?> INSTANCE = new ZipWith<>();

    private ZipWith() {
    }

    @Override
    public Iterable<C> checkedApply(Fn2<? super A, ? super B, ? extends C> zipper, Iterable<A> as,
                                    Iterable<B> bs) {
        return () -> new ZippingIterator<>(zipper, as.iterator(), bs.iterator());
    }

    @SuppressWarnings("unchecked")
    public static <A, B, C> ZipWith<A, B, C> zipWith() {
        return (ZipWith<A, B, C>) INSTANCE;
    }

    public static <A, B, C> Fn2<Iterable<A>, Iterable<B>, Iterable<C>> zipWith(
            Fn2<? super A, ? super B, ? extends C> zipper) {
        return ZipWith.<A, B, C>zipWith().apply(zipper);
    }

    public static <A, B, C> Fn1<Iterable<B>, Iterable<C>> zipWith(Fn2<? super A, ? super B, ? extends C> zipper,
                                                                  Iterable<A> as) {
        return ZipWith.<A, B, C>zipWith(zipper).apply(as);
    }

    public static <A, B, C> Iterable<C> zipWith(Fn2<? super A, ? super B, ? extends C> zipper, Iterable<A> as,
                                                Iterable<B> bs) {
        return ZipWith.<A, B, C>zipWith(zipper, as).apply(bs);
    }
}
