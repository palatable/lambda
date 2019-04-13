package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.builtin.fn3.CmpEqBy;
import com.jnape.palatable.lambda.functions.specialized.BiPredicate;
import com.jnape.palatable.lambda.functions.specialized.Predicate;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn3.CmpEqBy.cmpEqBy;

/**
 * Given two {@link Comparable} values of type <code>A</code>, return <code>true</code> if the first value is strictly
 * equal to the second value (according to {@link Comparable#compareTo(Object)}; otherwise, return false.
 *
 * @param <A> the value type
 * @see CmpEqBy
 * @see LT
 * @see GT
 */
public final class CmpEq<A extends Comparable<A>> implements BiPredicate<A, A> {

    private static final CmpEq<?> INSTANCE = new CmpEq<>();

    private CmpEq() {
    }

    @Override
    public Boolean apply(A x, A y) {
        return cmpEqBy(id(), x, y);
    }

    @SuppressWarnings("unchecked")
    public static <A extends Comparable<A>> CmpEq<A> cmpEq() {
        return (CmpEq<A>) INSTANCE;
    }

    public static <A extends Comparable<A>> Predicate<A> cmpEq(A x) {
        return CmpEq.<A>cmpEq().apply(x);
    }

    public static <A extends Comparable<A>> Boolean cmpEq(A x, A y) {
        return cmpEq(x).apply(y);
    }
}
