package com.jnape.palatable.lambda.semigroup.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.SemigroupFactory;
import com.jnape.palatable.lambda.semigroup.Semigroup;

import java.util.Comparator;

import static com.jnape.palatable.lambda.functions.builtin.fn3.LTWith.ltWith;

/**
 * Given a comparator for some type <code>A</code>, produce a {@link Semigroup} over <code>A</code> that chooses
 * between two values <code>x</code> and <code>y</code> via the following rules:
 * <ul>
 * <li>If <code>x</code> is strictly less than <code>y</code> in terms of <code>B</code>, return <code>y</code></li>
 * <li>Otherwise, return <code>x</code></li>
 * </ul>
 *
 * @param <A> the value type
 * @see Max
 * @see MaxBy
 * @see MinWith
 */
public final class MaxWith<A> implements SemigroupFactory<Comparator<A>, A> {

    private static final MaxWith<?> INSTANCE = new MaxWith<>();

    private MaxWith() {
    }

    @SuppressWarnings("unchecked")
    public static <A> MaxWith<A> maxWith() {
        return (MaxWith<A>) INSTANCE;
    }

    public static <A> Semigroup<A> maxWith(Comparator<A> compareFn) {
        return MaxWith.<A>maxWith().apply(compareFn);
    }

    public static <A> Fn1<A, A> maxWith(Comparator<A> compareFn, A x) {
        return MaxWith.maxWith(compareFn).apply(x);
    }

    public static <A> A maxWith(Comparator<A> compareFn, A x, A y) {
        return maxWith(compareFn, x).apply(y);
    }

    @Override
    public Semigroup<A> checkedApply(Comparator<A> comparator) {
        return (x, y) -> ltWith(comparator, y, x) ? y : x;
    }
}