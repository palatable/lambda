package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.MonadicFunction;
import com.jnape.palatable.lambda.iterators.ReversingIterator;

import java.util.Iterator;

public final class Reverse<A> extends MonadicFunction<Iterable<A>, Iterable<A>> {

    @Override
    public final Iterable<A> apply(final Iterable<A> as) {
        return new Iterable<A>() {
            @Override
            public Iterator<A> iterator() {
                return new ReversingIterator<A>(as.iterator());
            }
        };
    }

    public static <A> Reverse<A> reverse() {
        return new Reverse<A>();
    }

    public static <A> Iterable<A> reverse(final Iterable<A> as) {
        return Reverse.<A>reverse().apply(as);
    }
}
