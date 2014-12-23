package com.jnape.palatable.lambda.functions.builtin.dyadic;

import com.jnape.palatable.lambda.functions.DyadicFunction;
import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.iterators.MappingIterator;

public final class Map<A, B> implements DyadicFunction<MonadicFunction<? super A, ? extends B>, Iterable<A>, Iterable<B>> {

    @Override
    public final Iterable<B> apply(final MonadicFunction<? super A, ? extends B> function, final Iterable<A> as) {
        return () -> new MappingIterator<>(function, as.iterator());
    }

    public static <A, B> Map<A, B> map() {
        return new Map<>();
    }

    public static <A, B> MonadicFunction<Iterable<A>, Iterable<B>> map(
            MonadicFunction<? super A, ? extends B> function) {
        return Map.<A, B>map().apply(function);
    }

    public static <A, B> Iterable<B> map(MonadicFunction<? super A, ? extends B> function, Iterable<A> as) {
        return map(function).apply(as);
    }
}
