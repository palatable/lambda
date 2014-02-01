package com.jnape.palatable.lambda.iterables;

import com.jnape.palatable.lambda.iterators.ImmutableIterator;
import com.jnape.palatable.lambda.tuples.Tuple2;

import java.util.Iterator;

import static com.jnape.palatable.lambda.tuples.Tuple2.tuple;

public class CartesianProductIterable<A, B> implements Iterable<Tuple2<A, B>> {

    private final Iterable<A> as;
    private final Iterable<B> bs;

    public CartesianProductIterable(Iterable<A> as, Iterable<B> bs) {
        this.as = as;
        this.bs = bs;
    }

    @Override
    public Iterator<Tuple2<A, B>> iterator() {
        final Iterator<A> asIterator = as.iterator();

        return new ImmutableIterator<Tuple2<A, B>>() {
            private A currentA = null;
            private Iterator<B> bsIterator = bs.iterator();

            @Override
            public boolean hasNext() {
                return asIterator.hasNext() || currentA != null && bsIterator.hasNext();
            }

            @Override
            public Tuple2<A, B> next() {
                if (currentA == null)
                    currentA = asIterator.next();
                if (!bsIterator.hasNext() && asIterator.hasNext()) {
                    bsIterator = bs.iterator();
                    currentA = asIterator.next();
                }

                return tuple(currentA, bsIterator.next());
            }
        };
    }

    public static <A, B> CartesianProductIterable<A, B> cartesianProduct(Iterable<A> as, Iterable<B> bs) {
        return new CartesianProductIterable<A, B>(as, bs);
    }
}
