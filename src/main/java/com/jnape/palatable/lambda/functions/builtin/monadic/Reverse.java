package com.jnape.palatable.lambda.functions.builtin.monadic;

import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.iterators.ReversingIterator;

/**
 * Given an <code>Iterable</code>, return a reversed representation of that <code>Iterable</code>. Note that reversing
 * is deferred until the returned <code>Iterable</code> is iterated.
 *
 * @param <A> The Iterable element type
 */
public final class Reverse<A> implements MonadicFunction<Iterable<A>, Iterable<A>> {

    @Override
    public Iterable<A> apply(Iterable<A> as) {
        return () -> new ReversingIterator<>(as.iterator());
    }

    public static <A> Reverse<A> reverse() {
        return new Reverse<>();
    }

    public static <A> Iterable<A> reverse(Iterable<A> as) {
        return Reverse.<A>reverse().apply(as);
    }
}
