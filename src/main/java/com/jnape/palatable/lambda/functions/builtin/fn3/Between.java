package com.jnape.palatable.lambda.functions.builtin.fn3;

import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functions.specialized.BiPredicate;
import com.jnape.palatable.lambda.functions.specialized.Predicate;

import static com.jnape.palatable.lambda.functions.builtin.fn3.Clamp.clamp;

/**
 * Given two bounds and a value, return whether or not the value is greater than or equal to the lower bound and less
 * than or equal to the upper bound.
 *
 * @param <A> the bounds and input type
 */
public final class Between<A extends Comparable<A>> implements Fn3<A, A, A, Boolean> {

    private static final Between<?> INSTANCE = new Between<>();

    private Between() {
    }

    @Override
    public Boolean checkedApply(A lower, A upper, A a) {
        return clamp(lower, upper, a).equals(a);
    }

    @SuppressWarnings("unchecked")
    public static <A extends Comparable<A>> Between<A> between() {
        return (Between<A>) INSTANCE;
    }

    public static <A extends Comparable<A>> BiPredicate<A, A> between(A lower) {
        return Between.<A>between().apply(lower)::apply;
    }

    public static <A extends Comparable<A>> Predicate<A> between(A lower, A upper) {
        return between(lower).apply(upper);
    }

    public static <A extends Comparable<A>> Boolean between(A lower, A upper, A a) {
        return between(lower, upper).apply(a);
    }
}
