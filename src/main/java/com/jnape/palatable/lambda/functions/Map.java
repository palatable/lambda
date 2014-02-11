package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.DyadicFunction;
import com.jnape.palatable.lambda.MonadicFunction;
import com.jnape.palatable.lambda.iterators.MappingIterator;

import java.util.Iterator;

public class Map<A, B> extends DyadicFunction<MonadicFunction<? super A, ? extends B>, Iterable<A>, Iterable<B>> {

    @Override
    public Iterable<B> apply(final MonadicFunction<? super A, ? extends B> function, final Iterable<A> as) {
        return new Iterable<B>() {
            @Override
            public Iterator<B> iterator() {
                return new MappingIterator<A, B>(function, as.iterator());
            }
        };
    }

    public static <A, B> Iterable<B> map(final MonadicFunction<? super A, ? extends B> function, final Iterable<A> as) {
        return new Map<A, B>().apply(function, as);
    }
}
