package com.jnape.palatable.lambda.functions.builtin.dyadic;

import com.jnape.palatable.lambda.functions.DyadicFunction;
import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.iterators.FilteringIterator;

public final class Filter<A> implements DyadicFunction<MonadicFunction<? super A, Boolean>, Iterable<A>, Iterable<A>> {

    @Override
    public final Iterable<A> apply(final MonadicFunction<? super A, Boolean> predicate, final Iterable<A> as) {
        return () -> new FilteringIterator<>(predicate, as.iterator());
    }

    public static <A> Filter<A> filter() {
        return new Filter<>();
    }

    public static <A> MonadicFunction<Iterable<A>, Iterable<A>> filter(
            final MonadicFunction<? super A, Boolean> predicate) {
        return Filter.<A>filter().apply(predicate);
    }

    public static <A> Iterable<A> filter(final MonadicFunction<? super A, Boolean> predicate, final Iterable<A> as) {
        return filter(predicate).apply(as);
    }
}
