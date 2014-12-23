package com.jnape.palatable.lambda.functions.builtin.dyadic;

import com.jnape.palatable.lambda.functions.DyadicFunction;
import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.iterators.UnfoldingIterator;

public final class Unfoldr<A> implements DyadicFunction<MonadicFunction<? super A, ? extends A>, A, Iterable<A>> {

    @Override
    public final Iterable<A> apply(final MonadicFunction<? super A, ? extends A> fn, final A seed) {
        return () -> new UnfoldingIterator<>(fn, seed);
    }

    public static <A> Unfoldr<A> unfoldr() {
        return new Unfoldr<>();
    }

    public static <A> MonadicFunction<A, Iterable<A>> unfoldr(MonadicFunction<? super A, ? extends A> fn) {
        return Unfoldr.<A>unfoldr().apply(fn);
    }

    public static <A> Iterable<A> unfoldr(MonadicFunction<? super A, ? extends A> fn, A seed) {
        return unfoldr(fn).apply(seed);
    }
}
