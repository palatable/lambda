package com.jnape.palatable.lambda.functions.builtin.monadic;

import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.iterators.ReversingIterator;

public final class Reverse<A> implements MonadicFunction<Iterable<A>, Iterable<A>> {

    @Override
    public final Iterable<A> apply(final Iterable<A> as) {
        return () -> new ReversingIterator<>(as.iterator());
    }

    public static <A> Reverse<A> reverse() {
        return new Reverse<>();
    }

    public static <A> Iterable<A> reverse(final Iterable<A> as) {
        return Reverse.<A>reverse().apply(as);
    }
}
