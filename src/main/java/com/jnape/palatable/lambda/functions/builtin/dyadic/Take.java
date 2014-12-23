package com.jnape.palatable.lambda.functions.builtin.dyadic;

import com.jnape.palatable.lambda.functions.DyadicFunction;
import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.iterators.TakingIterator;

public final class Take<A> implements DyadicFunction<Integer, Iterable<A>, Iterable<A>> {

    @Override
    public final Iterable<A> apply(final Integer n, final Iterable<A> as) {
        return () -> new TakingIterator<>(n, as.iterator());
    }

    public static <A> Take<A> take() {
        return new Take<>();
    }

    public static <A> MonadicFunction<Iterable<A>, Iterable<A>> take(int n) {
        return Take.<A>take().apply(n);
    }

    public static <A> Iterable<A> take(int n, Iterable<A> as) {
        return Take.<A>take(n).apply(as);
    }
}
