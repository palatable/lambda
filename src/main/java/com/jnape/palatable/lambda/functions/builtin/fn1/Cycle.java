package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.iterators.CyclicIterator;

import static java.util.Arrays.asList;

/**
 * Given an <code>Iterable</code>, return an infinite <code>Iterable</code> that repeatedly cycles its elements, in
 * order.
 *
 * @param <A> The Iterable element type
 */
public final class Cycle<A> implements Fn1<Iterable<A>, Iterable<A>> {

    private Cycle() {
    }

    @Override
    public Iterable<A> apply(Iterable<A> as) {
        return () -> new CyclicIterator<>(as.iterator());
    }

    public static <A> Cycle<A> cycle() {
        return new Cycle<>();
    }

    public static <A> Iterable<A> cycle(Iterable<A> as) {
        return Cycle.<A>cycle().apply(as);
    }

    @SafeVarargs
    public static <A> Iterable<A> cycle(A... as) {
        return cycle(asList(as));
    }
}
