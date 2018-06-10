package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.monoid.Monoid;

import java.util.Collections;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Distinct.distinct;
import static com.jnape.palatable.lambda.monoid.builtin.Concat.concat;

/**
 * Given two {@link Iterable}s, return the union of all unique occurrences of elements between them. Note that this
 * operation preserves order, so the unique elements of the first {@link Iterable} are iterated before the unique
 * elements of the second {@link Iterable}.
 *
 * @param <A> the {@link Iterable} element type)
 */
public final class Union<A> implements Monoid<Iterable<A>> {

    private static final Union INSTANCE = new Union();

    private Union() {
    }

    @Override
    public Iterable<A> identity() {
        return Collections::emptyIterator;
    }

    @Override
    public Iterable<A> apply(Iterable<A> xs, Iterable<A> ys) {
        return distinct(concat(xs, ys));
    }

    @SuppressWarnings("unchecked")
    public static <A> Union<A> union() {
        return INSTANCE;
    }

    public static <A> Fn1<Iterable<A>, Iterable<A>> union(Iterable<A> xs) {
        return Union.<A>union().apply(xs);
    }

    public static <A> Iterable<A> union(Iterable<A> xs, Iterable<A> ys) {
        return union(xs).apply(ys);
    }
}
