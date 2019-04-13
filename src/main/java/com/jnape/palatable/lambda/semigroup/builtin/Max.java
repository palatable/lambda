package com.jnape.palatable.lambda.semigroup.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.semigroup.Semigroup;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.semigroup.builtin.MaxBy.maxBy;

/**
 * A {@link Semigroup} over <code>A</code> that chooses between two values <code>x</code> and <code>y</code> via the
 * following rules:
 * <ul>
 * <li>If <code>x</code> is strictly less than <code>y</code>, return <code>y</code></li>
 * <li>Otherwise, return <code>x</code></li>
 * </ul>
 *
 * @param <A> the value type
 * @see MaxBy
 * @see Min
 */
public final class Max<A extends Comparable<A>> implements Semigroup<A> {

    private static final Max<?> INSTANCE = new Max<>();

    private Max() {
    }

    @Override
    public A apply(A x, A y) {
        return maxBy(id(), x, y);
    }

    @SuppressWarnings("unchecked")
    public static <A extends Comparable<A>> Max<A> max() {
        return (Max<A>) INSTANCE;
    }

    public static <A extends Comparable<A>> Fn1<A, A> max(A x) {
        return Max.<A>max().apply(x);
    }

    public static <A extends Comparable<A>> A max(A x, A y) {
        return max(x).apply(y);
    }
}
