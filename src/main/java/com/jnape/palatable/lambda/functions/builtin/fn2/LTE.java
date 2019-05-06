package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.builtin.fn3.LTEBy;
import com.jnape.palatable.lambda.functions.specialized.BiPredicate;
import com.jnape.palatable.lambda.functions.specialized.Predicate;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn3.LTEBy.lteBy;

/**
 * Given two {@link Comparable} values of type <code>A</code>, return <code>true</code> if the second value is less than
 * or equal to the first value according to {@link Comparable#compareTo(Object)} otherwise, return false.
 *
 * @param <A> the value typ
 * @see LTEBy
 * @see GTE
 */
public final class LTE<A extends Comparable<A>> implements BiPredicate<A, A> {

    private static final LTE<?> INSTANCE = new LTE<>();

    private LTE() {
    }

    @Override
    public Boolean checkedApply(A y, A x) {
        return lteBy(id(), y, x);
    }

    @SuppressWarnings("unchecked")
    public static <A extends Comparable<A>> LTE<A> lte() {
        return (LTE<A>) INSTANCE;
    }

    public static <A extends Comparable<A>> Predicate<A> lte(A y) {
        return LTE.<A>lte().apply(y);
    }

    public static <A extends Comparable<A>> Boolean lte(A y, A x) {
        return lte(y).apply(x);
    }
}
