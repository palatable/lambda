package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.DyadicFunction;
import com.jnape.palatable.lambda.MonadicFunction;
import com.jnape.palatable.lambda.iterators.FilteringIterator;

import java.util.Iterator;

public class Filter<A> extends DyadicFunction<MonadicFunction<? super A, Boolean>, Iterable<A>, Iterable<A>> {

    @Override
    public Iterable<A> apply(final MonadicFunction<? super A, Boolean> predicate, final Iterable<A> as) {
        return new Iterable<A>() {
            @Override
            public Iterator<A> iterator() {
                return new FilteringIterator<A>(predicate, as.iterator());
            }
        };
    }

    public static <A> Iterable<A> filter(final MonadicFunction<? super A, Boolean> predicate, final Iterable<A> as) {
        return new Filter<A>().apply(predicate, as);
    }
}
