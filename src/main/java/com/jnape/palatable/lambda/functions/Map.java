package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.DyadicFunction;
import com.jnape.palatable.lambda.MonadicFunction;
import com.jnape.palatable.lambda.iterators.MappingIterator;

import java.util.Iterator;

public final class Map<A, B> extends DyadicFunction<MonadicFunction<? super A, ? extends B>, Iterable<A>, Iterable<B>> {

    @Override
    public final Iterable<B> apply(final MonadicFunction<? super A, ? extends B> function, final Iterable<A> as) {
        return new Iterable<B>() {
            @Override
            public Iterator<B> iterator() {
                return new MappingIterator<A, B>(function, as.iterator());
            }
        };
    }

    public static <A, B> Map<A, B> map() {
        return new Map<A, B>();
    }

    public static <A, B> MonadicFunction<Iterable<A>, Iterable<B>> map(
            MonadicFunction<? super A, ? extends B> function) {
        return Map.<A, B>map().partial(function);
    }

    public static <A, B> Iterable<B> map(MonadicFunction<? super A, ? extends B> function, Iterable<A> as) {
        return map(function).apply(as);
    }
}
