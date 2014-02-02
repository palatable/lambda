package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.DyadicFunction;
import com.jnape.palatable.lambda.iterators.ImmutableIterator;
import com.jnape.palatable.lambda.tuples.Tuple2;

import java.util.Iterator;

import static com.jnape.palatable.lambda.tuples.Tuple2.tuple;

public class Zip {

    public static <A, B> Iterable<Tuple2<A, B>> zip(Iterable<A> as, Iterable<B> bs) {
        DyadicFunction<A, B, Tuple2<A, B>> zipper = new DyadicFunction<A, B, Tuple2<A, B>>() {
            @Override
            public Tuple2<A, B> apply(A a, B b) {
                return tuple(a, b);
            }
        };
        return zipWith(zipper, as, bs);
    }

    public static <A, B, C> Iterable<C> zipWith(final DyadicFunction<? super A, ? super B, ? extends C> zipper,
                                                final Iterable<A> as,
                                                final Iterable<B> bs) {
        return new Iterable<C>() {
            @Override
            public Iterator<C> iterator() {
                final Iterator<A> asIterator = as.iterator();
                final Iterator<B> bsIterator = bs.iterator();
                return new ImmutableIterator<C>() {
                    @Override
                    public boolean hasNext() {
                        return asIterator.hasNext() && bsIterator.hasNext();
                    }

                    @Override
                    public C next() {
                        return zipper.apply(asIterator.next(), bsIterator.next());
                    }
                };
            }
        };
    }
}
