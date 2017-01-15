package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.iterators.GroupingIterator;

/**
 * Lazily group the <code>Iterable</code> by returning an <code>Iterable</code> of smaller <code>Iterable</code>s of
 * size <code>k</code>. Note that groups are <em>not</em> padded; that is, if <code>k &gt;= n</code>, where
 * <code>n</code> is the number of remaining elements, the final <code>Iterable</code> will have only <code>n</code>
 * elements.
 *
 * @param <A> The Iterable element type
 */
public final class InGroupsOf<A> implements Fn2<Integer, Iterable<A>, Iterable<Iterable<A>>> {

    private static final InGroupsOf INSTANCE = new InGroupsOf();

    private InGroupsOf() {
    }

    @Override
    public Iterable<Iterable<A>> apply(Integer k, Iterable<A> as) {
        return () -> new GroupingIterator<>(k, as.iterator());
    }

    @SuppressWarnings("unchecked")
    public static <A> InGroupsOf<A> inGroupsOf() {
        return INSTANCE;
    }

    public static <A> Fn1<Iterable<A>, Iterable<Iterable<A>>> inGroupsOf(Integer k) {
        return InGroupsOf.<A>inGroupsOf().apply(k);
    }

    public static <A> Iterable<Iterable<A>> inGroupsOf(Integer k, Iterable<A> as) {
        return InGroupsOf.<A>inGroupsOf(k).apply(as);
    }
}
