package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn1.Distinct;
import com.jnape.palatable.lambda.iteration.UnioningIterable;
import com.jnape.palatable.lambda.monoid.Monoid;

import java.util.Collections;

/**
 * Given two {@link Iterable Iterables} <code>xs</code> and <code>ys</code>, return the {@link Concat concatenation} of
 * the {@link Distinct distinct} elements of both <code>xs</code> and <code>ys</code>.
 *
 * @param <A> the {@link Iterable} element type
 */
public final class Union<A> implements Monoid<Iterable<A>> {

    private static final Union<?> INSTANCE = new Union<>();

    private Union() {
    }

    @Override
    public Iterable<A> identity() {
        return Collections::emptyIterator;
    }

    @Override
    public Iterable<A> checkedApply(Iterable<A> xs, Iterable<A> ys) {
        return new UnioningIterable<>(xs, ys);
    }

    @SuppressWarnings("unchecked")
    public static <A> Union<A> union() {
        return (Union<A>) INSTANCE;
    }

    public static <A> Fn1<Iterable<A>, Iterable<A>> union(Iterable<A> xs) {
        return Union.<A>union().apply(xs);
    }

    public static <A> Iterable<A> union(Iterable<A> xs, Iterable<A> ys) {
        return union(xs).apply(ys);
    }
}
