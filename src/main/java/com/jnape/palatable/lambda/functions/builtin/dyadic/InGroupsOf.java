package com.jnape.palatable.lambda.functions.builtin.dyadic;

import com.jnape.palatable.lambda.functions.DyadicFunction;
import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.iterators.GroupingIterator;

/**
 * Lazily group the <code>Iterable</code> by returning an <code>Iterable</code> of smaller <code>Iterable</code>s of
 * size <code>k</code>. Note that groups are <em>not</em> padded; that is, if <code>k &gt;= n</code>, where <code>n</code>
 * is the number of remaining elements, the final <code>Iterable</code> will have only <code>n</code> elements.
 *
 * @param <A> The Iterable element type
 */
public final class InGroupsOf<A> implements DyadicFunction<Integer, Iterable<A>, Iterable<Iterable<A>>> {

    private InGroupsOf() {
    }

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
