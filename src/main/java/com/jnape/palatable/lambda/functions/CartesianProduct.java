package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.iterators.ImmutableIterator;
import com.jnape.palatable.lambda.tuples.Tuple2;

import java.util.Iterator;

import static com.jnape.palatable.lambda.tuples.Tuple2.tuple;

public class CartesianProduct {

    public static <A, B> Iterable<Tuple2<A, B>> cartesianProduct(final Iterable<A> as, final Iterable<B> bs) {
        final Iterator<A> asIterator = as.iterator();
        return new Iterable<Tuple2<A, B>>() {
            @Override
            public Iterator<Tuple2<A, B>> iterator() {
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
        };
    }

}
