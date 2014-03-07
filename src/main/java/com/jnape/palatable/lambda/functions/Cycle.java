package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.MonadicFunction;
import com.jnape.palatable.lambda.iterators.CyclicIterator;

import java.util.Iterator;

import static com.jnape.palatable.lambda.staticfactory.IterableFactory.iterable;

public final class Cycle<A> extends MonadicFunction<Iterable<A>, Iterable<A>> {

    @Override
    public final Iterable<A> apply(final Iterable<A> as) {
        return new Iterable<A>() {
            @Override
            public Iterator<A> iterator() {
                return new CyclicIterator<A>(as.iterator());
            }
        };
    }

    public static <A> Cycle<A> cycle() {
        return new Cycle<A>();
    }

    public static <A> Iterable<A> cycle(Iterable<A> as) {
        return Cycle.<A>cycle().apply(as);
    }

    public static <A> Iterable<A> cycle(A... as) {
        return cycle(iterable(as));
    }
}
