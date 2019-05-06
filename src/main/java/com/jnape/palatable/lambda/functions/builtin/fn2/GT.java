package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.builtin.fn3.GTBy;
import com.jnape.palatable.lambda.functions.specialized.BiPredicate;
import com.jnape.palatable.lambda.functions.specialized.Predicate;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn3.GTBy.gtBy;

/**
 * Given two {@link Comparable} values of type <code>A</code>, return <code>true</code> if the second value is strictly
 * greater than the first value; otherwise, return false.
 *
 * @param <A> the value type
 * @see GTBy
 * @see LT
 */
public final class GT<A extends Comparable<A>> implements BiPredicate<A, A> {

    private static final GT<?> INSTANCE = new GT<>();

    private GT() {
    }

    @Override
    public Boolean checkedApply(A y, A x) {
        return gtBy(id(), y, x);
    }

    @SuppressWarnings("unchecked")
    public static <A extends Comparable<A>> GT<A> gt() {
        return (GT<A>) INSTANCE;
    }

    public static <A extends Comparable<A>> Predicate<A> gt(A y) {
        return GT.<A>gt().apply(y);
    }

    public static <A extends Comparable<A>> Boolean gt(A y, A x) {
        return gt(y).apply(x);
    }
}
