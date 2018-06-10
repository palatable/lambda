package com.jnape.palatable.lambda.semigroup.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.SemigroupFactory;
import com.jnape.palatable.lambda.semigroup.Semigroup;

import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn3.GTBy.gtBy;

/**
 * Given a mapping function from some type <code>A</code> to some {@link Comparable} type <code>B</code>, produce a
 * {@link Semigroup} over <code>A</code> that chooses between two values <code>x</code> and <code>y</code> via the
 * following rules:
 * <ul>
 * <li>If <code>x</code> is strictly greater than <code>y</code> in terms of <code>B</code>, return <code>y</code></li>
 * <li>Otherwise, return <code>x</code></li>
 * </ul>
 *
 * @param <A> the value type
 * @param <B> the mapped comparison type
 * @see Min
 * @see MaxBy
 */
public final class MinBy<A, B extends Comparable<B>> implements SemigroupFactory<Function<? super A, ? extends B>, A> {

    private static final MinBy INSTANCE = new MinBy();

    private MinBy() {
    }

    @Override
    public Semigroup<A> apply(Function<? super A, ? extends B> compareFn) {
        return (x, y) -> gtBy(compareFn, x, y) ? y : x;
    }

    @SuppressWarnings("unchecked")
    public static <A, B extends Comparable<B>> MinBy<A, B> minBy() {
        return INSTANCE;
    }

    public static <A, B extends Comparable<B>> Semigroup<A> minBy(
            Function<? super A, ? extends B> compareFn) {
        return MinBy.<A, B>minBy().apply(compareFn);
    }

    public static <A, B extends Comparable<B>> Fn1<A, A> minBy(Function<? super A, ? extends B> compareFn, A x) {
        return MinBy.<A, B>minBy(compareFn).apply(x);
    }

    public static <A, B extends Comparable<B>> A minBy(Function<? super A, ? extends B> compareFn, A x, A y) {
        return minBy(compareFn, x).apply(y);
    }
}
