package com.jnape.palatable.lambda.functions.builtin.fn3;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;

import static com.jnape.palatable.lambda.semigroup.builtin.Max.max;
import static com.jnape.palatable.lambda.semigroup.builtin.Min.min;

/**
 * Given two bounds and a value, "clamp" the value between the bounds via the following algorithm:
 * - if the value is strictly less than the lower bound, return the lower bound
 * - if the value is strictly greater than the upper bound, return the upper bound
 * - otherwise, return the value
 *
 * @param <A> the bounds and input type
 */
public final class Clamp<A extends Comparable<A>> implements Fn3<A, A, A, A> {

    private static final Clamp INSTANCE = new Clamp<>();

    private Clamp() {
    }

    @Override
    public A apply(A lower, A upper, A a) {
        return max(min(lower, upper)).fmap(min(max(lower, upper))).apply(a);
    }

    @SuppressWarnings("unchecked")
    public static <A extends Comparable<A>> Clamp<A> clamp() {
        return INSTANCE;
    }

    public static <A extends Comparable<A>> Fn2<A, A, A> clamp(A lower) {
        return Clamp.<A>clamp().apply(lower);
    }

    public static <A extends Comparable<A>> Fn1<A, A> clamp(A lower, A upper) {
        return clamp(lower).apply(upper);
    }

    public static <A extends Comparable<A>> A clamp(A lower, A upper, A a) {
        return clamp(lower, upper).apply(a);
    }
}
