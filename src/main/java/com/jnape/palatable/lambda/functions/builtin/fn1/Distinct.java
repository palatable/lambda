package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.internal.iteration.DistinctIterable;

/**
 * Return an {@link Iterable} of the distinct values from the given input {@link Iterable}.
 *
 * @param <A> the Iterable element type
 */
public final class Distinct<A> implements Fn1<Iterable<A>, Iterable<A>> {
    private static final Distinct<?> INSTANCE = new Distinct<>();

    private Distinct() {
    }

    @Override
    public Iterable<A> checkedApply(Iterable<A> as) {
        return new DistinctIterable<>(as);
    }

    @SuppressWarnings("unchecked")
    public static <A> Distinct<A> distinct() {
        return (Distinct<A>) INSTANCE;
    }

    public static <A> Iterable<A> distinct(Iterable<A> as) {
        return Distinct.<A>distinct().apply(as);
    }
}
