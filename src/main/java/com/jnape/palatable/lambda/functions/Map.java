package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.MonadicFunction;
import com.jnape.palatable.lambda.iterators.ImmutableIterator;

import java.util.Iterator;

public class Map {

    public static <A, B> Iterable<B> map(final MonadicFunction<? super A, ? extends B> function, final Iterable<A> as) {
        final Iterator<A> asIterator = as.iterator();
        return new Iterable<B>() {
            @Override
            public Iterator<B> iterator() {
                return new ImmutableIterator<B>() {
                    @Override
                    public boolean hasNext() {
                        return asIterator.hasNext();
                    }

                    @Override
                    public B next() {
                        return function.apply(asIterator.next());
                    }
                };
            }
        };
    }

}
