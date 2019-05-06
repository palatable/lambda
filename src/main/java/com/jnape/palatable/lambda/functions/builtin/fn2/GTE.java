package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.builtin.fn3.GTEBy;
import com.jnape.palatable.lambda.functions.specialized.BiPredicate;
import com.jnape.palatable.lambda.functions.specialized.Predicate;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn3.GTEBy.gteBy;

/**
 * Given two {@link Comparable} values of type <code>A</code>, return <code>true</code> if the second value is greater
 * than or equal to the first value according to {@link Comparable#compareTo(Object)}; otherwise, return false.
 *
 * @param <A> the value type
 * @see GTEBy
 * @see LTE
 */
public final class GTE<A extends Comparable<A>> implements BiPredicate<A, A> {

    private static final GTE<?> INSTANCE = new GTE<>();

    private GTE() {
    }

    @Override
    public Boolean checkedApply(A y, A x) {
        return gteBy(id(), y, x);
    }

    @SuppressWarnings("unchecked")
    public static <A extends Comparable<A>> GTE<A> gte() {
        return (GTE<A>) INSTANCE;
    }

    public static <A extends Comparable<A>> Predicate<A> gte(A y) {
        return GTE.<A>gte().apply(y);
    }

    public static <A extends Comparable<A>> Boolean gte(A y, A x) {
        return gte(y).apply(x);
    }
}
