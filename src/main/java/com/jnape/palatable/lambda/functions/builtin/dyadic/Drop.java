package com.jnape.palatable.lambda.functions.builtin.dyadic;

import com.jnape.palatable.lambda.functions.DyadicFunction;
import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.iterators.DroppingIterator;

public final class Drop<A> implements DyadicFunction<Integer, Iterable<A>, Iterable<A>> {

    @Override
    public final Iterable<A> apply(final Integer n, final Iterable<A> as) {
        return () -> new DroppingIterator<>(n, as.iterator());
    }

    public static <A> Drop<A> drop() {
        return new Drop<>();
    }

    public static <A> MonadicFunction<Iterable<A>, Iterable<A>> drop(int n) {
        return Drop.<A>drop().apply(n);
    }

    public static <A> Iterable<A> drop(int n, Iterable<A> as) {
        return Drop.<A>drop(n).apply(as);
    }
}
