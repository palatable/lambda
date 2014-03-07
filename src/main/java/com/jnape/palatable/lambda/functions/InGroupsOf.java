package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.DyadicFunction;
import com.jnape.palatable.lambda.MonadicFunction;
import com.jnape.palatable.lambda.iterators.GroupingIterator;

import java.util.Iterator;

public final class InGroupsOf<A> extends DyadicFunction<Integer, Iterable<A>, Iterable<Iterable<A>>> {

    @Override
    public final Iterable<Iterable<A>> apply(final Integer k, final Iterable<A> as) {
        return new Iterable<Iterable<A>>() {
            @Override
            public Iterator<Iterable<A>> iterator() {
                return new GroupingIterator<A>(k, as.iterator());
            }
        };
    }

    public static <A> InGroupsOf<A> inGroupsOf() {
        return new InGroupsOf<A>();
    }

    public static <A> MonadicFunction<Iterable<A>, Iterable<Iterable<A>>> inGroupsOf(Integer k) {
        return InGroupsOf.<A>inGroupsOf().partial(k);
    }

    public static <A> Iterable<Iterable<A>> inGroupsOf(Integer k, Iterable<A> as) {
        return InGroupsOf.<A>inGroupsOf(k).apply(as);
    }
}
