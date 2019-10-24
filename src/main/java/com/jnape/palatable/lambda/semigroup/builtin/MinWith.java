package com.jnape.palatable.lambda.semigroup.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.SemigroupFactory;
import com.jnape.palatable.lambda.semigroup.Semigroup;

import java.util.Comparator;

import static com.jnape.palatable.lambda.functions.builtin.fn3.GTWith.gtWith;

/**
 * Given a comparator for some type <code>A</code>, produce a {@link Semigroup} over <code>A</code> that chooses
 * between two values <code>x</code> and <code>y</code> via the following rules:
 * <ul>
 * <li>If <code>x</code> is strictly greater than <code>y</code> in terms of <code>B</code>, return <code>y</code></li>
 * <li>Otherwise, return <code>x</code></li>
 * </ul>
 *
 * @param <A> the value type
 * @see Min
 * @see MinBy
 * @see MaxBy
 */
public final class MinWith<A> implements SemigroupFactory<Comparator<A>, A> {

    private static final MinWith<?> INSTANCE = new MinWith<>();

    private MinWith() {
    }

    @SuppressWarnings("unchecked")
    public static <A> MinWith<A> minWith() {
        return (MinWith<A>) INSTANCE;
    }

    public static <A> Semigroup<A> minWith(Comparator<A> compareFn) {
        return MinWith.<A>minWith().apply(compareFn);
    }

    public static <A> Fn1<A, A> minWith(Comparator<A> compareFn, A x) {
        return MinWith.minWith(compareFn).apply(x);
    }

    public static <A> A minWith(Comparator<A> compareFn, A x, A y) {
        return minWith(compareFn, x).apply(y);
    }

    @Override
    public Semigroup<A> checkedApply(Comparator<A> comparator) {
        return (x, y) -> gtWith(comparator, y, x) ? y : x;
    }
}
