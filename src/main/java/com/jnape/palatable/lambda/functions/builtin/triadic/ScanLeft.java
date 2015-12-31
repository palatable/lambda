package com.jnape.palatable.lambda.functions.builtin.triadic;

import com.jnape.palatable.lambda.functions.DyadicFunction;
import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.functions.TriadicFunction;
import com.jnape.palatable.lambda.iterators.ScanningIterator;

public final class ScanLeft<A, B> implements TriadicFunction<DyadicFunction<? super B, ? super A, ? extends B>, B, Iterable<A>, Iterable<B>> {

    @Override
    public Iterable<B> apply(DyadicFunction<? super B, ? super A, ? extends B> fn, B b, Iterable<A> as) {
        return () -> new ScanningIterator<>(fn, b, as.iterator());
    }

    public static <A, B> ScanLeft<A, B> scanLeft() {
        return new ScanLeft<>();
    }

    public static <A, B> DyadicFunction<B, Iterable<A>, Iterable<B>> scanLeft(
            DyadicFunction<? super B, ? super A, ? extends B> fn) {
        return ScanLeft.<A, B>scanLeft().apply(fn);
    }

    public static <A, B> MonadicFunction<Iterable<A>, Iterable<B>> scanLeft(
            DyadicFunction<? super B, ? super A, ? extends B> fn, B b) {
        return scanLeft(fn).apply(b);
    }

    public static <A, B> Iterable<B> scanLeft(DyadicFunction<? super B, ? super A, ? extends B> fn,
                                              B b,
                                              Iterable<A> as) {
        return scanLeft(fn, b).apply(as);
    }
}
