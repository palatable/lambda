package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.builtin.fn3.LTBy;
import com.jnape.palatable.lambda.functions.specialized.BiPredicate;
import com.jnape.palatable.lambda.functions.specialized.Predicate;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn3.LTBy.ltBy;

/**
 * Given two {@link Comparable} values of type <code>A</code>, return <code>true</code> if the second value is strictly
 * less than the first value; otherwise, return false.
 *
 * @param <A> the value type
 * @see LTBy
 * @see GT
 */
public final class LT<A extends Comparable<A>> implements BiPredicate<A, A> {

    private static final LT<?> INSTANCE = new LT<>();

    private LT() {
    }

    @Override
    public Boolean apply(A y, A x) {
        return ltBy(id(), y, x);
    }

    @SuppressWarnings("unchecked")
    public static <A extends Comparable<A>> LT<A> lt() {
        return (LT<A>) INSTANCE;
    }

    public static <A extends Comparable<A>> Predicate<A> lt(A y) {
        return LT.<A>lt().apply(y);
    }

    public static <A extends Comparable<A>> Boolean lt(A y, A x) {
        return lt(y).apply(x);
    }
}
