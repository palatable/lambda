package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.MonadicFunction;
import com.jnape.palatable.lambda.iterators.PredicatedTakingIterator;

import java.util.Iterator;

public class Take {

    public static <A> Iterable<A> take(final int n, final Iterable<A> as) {
        return new Iterable<A>() {
            @Override
            public Iterator<A> iterator() {
                return new TakingIterator<A>(n, as.iterator());
            }
        };
    }

    public static <A> Iterable<A> takeWhile(final MonadicFunction<? super A, Boolean> predicate, final Iterable<A> as) {
        return new Iterable<A>() {
            @Override
            public Iterator<A> iterator() {
                return new PredicatedTakingIterator<A>(predicate, as.iterator());
            }
        };
    }
}
