package com.jnape.palatable.lambda.semigroup.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.semigroup.Semigroup;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.semigroup.builtin.MinBy.minBy;

/**
 * A {@link Semigroup} over <code>A</code> that chooses between two values <code>x</code> and <code>y</code> via the
 * following rules:
 * <ul>
 * <li>If <code>x</code> is strictly greater than <code>y</code>, return <code>y</code></li>
 * <li>Otherwise, return <code>x</code></li>
 * </ul>
 *
 * @param <A> the value type
 * @see MinBy
 * @see Max
 */
public final class Min<A extends Comparable<A>> implements Semigroup<A> {

    private static final Min<?> INSTANCE = new Min<>();

    private Min() {
    }

    @Override
    public A checkedApply(A x, A y) {
        return minBy(id(), x, y);
    }

    @SuppressWarnings("unchecked")
    public static <A extends Comparable<A>> Min<A> min() {
        return (Min<A>) INSTANCE;
    }

    public static <A extends Comparable<A>> Fn1<A, A> min(A x) {
        return Min.<A>min().apply(x);
    }

    public static <A extends Comparable<A>> A min(A x, A y) {
        return min(x).apply(y);
    }
}
