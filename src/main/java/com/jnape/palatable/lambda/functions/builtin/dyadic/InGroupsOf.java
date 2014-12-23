package com.jnape.palatable.lambda.functions.builtin.dyadic;

import com.jnape.palatable.lambda.functions.DyadicFunction;
import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.iterators.GroupingIterator;

public final class InGroupsOf<A> implements DyadicFunction<Integer, Iterable<A>, Iterable<Iterable<A>>> {

    @Override
    public final Iterable<Iterable<A>> apply(final Integer k, final Iterable<A> as) {
        return () -> new GroupingIterator<>(k, as.iterator());
    }

    public static <A> InGroupsOf<A> inGroupsOf() {
        return new InGroupsOf<>();
    }

    public static <A> MonadicFunction<Iterable<A>, Iterable<Iterable<A>>> inGroupsOf(Integer k) {
        return InGroupsOf.<A>inGroupsOf().apply(k);
    }

    public static <A> Iterable<Iterable<A>> inGroupsOf(Integer k, Iterable<A> as) {
        return InGroupsOf.<A>inGroupsOf(k).apply(as);
    }
}
