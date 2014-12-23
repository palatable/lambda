package com.jnape.palatable.lambda.functions.builtin.monadic;

import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.iterators.CyclicIterator;

import static java.util.Arrays.asList;

public final class Cycle<A> implements MonadicFunction<Iterable<A>, Iterable<A>> {

    @Override
    public final Iterable<A> apply(final Iterable<A> as) {
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
