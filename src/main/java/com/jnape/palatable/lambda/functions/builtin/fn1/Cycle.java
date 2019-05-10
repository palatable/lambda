package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.internal.iteration.CyclicIterable;

import static java.util.Arrays.asList;

/**
 * Given an <code>Iterable</code>, return an infinite <code>Iterable</code> that repeatedly cycles its elements, in
 * order.
 *
 * @param <A> The Iterable element type
 */
public final class Cycle<A> implements Fn1<Iterable<A>, Iterable<A>> {

    private static final Cycle<?> INSTANCE = new Cycle<>();

    private Cycle() {
    }

    @Override
    public Iterable<A> checkedApply(Iterable<A> as) {
        return new CyclicIterable<>(as);
    }

    @SuppressWarnings("unchecked")
    public static <A> Cycle<A> cycle() {
        return (Cycle<A>) INSTANCE;
    }

    public static <A> Iterable<A> cycle(Iterable<A> as) {
        return Cycle.<A>cycle().apply(as);
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <A> Iterable<A> cycle(A... as) {
        return cycle(asList(as));
    }
}
