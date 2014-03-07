package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.DyadicFunction;
import com.jnape.palatable.lambda.MonadicFunction;
import com.jnape.palatable.lambda.iterators.CombinatorialIterator;
import com.jnape.palatable.lambda.tuples.Tuple2;

import java.util.Iterator;

public final class CartesianProduct<A, B> extends DyadicFunction<Iterable<A>, Iterable<B>, Iterable<Tuple2<A, B>>> {

    @Override
    public final Iterable<Tuple2<A, B>> apply(final Iterable<A> as, final Iterable<B> bs) {
        return new Iterable<Tuple2<A, B>>() {
            @Override
            public Iterator<Tuple2<A, B>> iterator() {
                return new CombinatorialIterator<A, B>(as.iterator(), bs.iterator());
            }
        };
    }

    public static <A, B> CartesianProduct<A, B> cartesianProduct() {
        return new CartesianProduct<A, B>();
    }

    public static <A, B> MonadicFunction<Iterable<B>, Iterable<Tuple2<A, B>>> cartesianProduct(Iterable<A> as) {
        return CartesianProduct.<A, B>cartesianProduct().partial(as);
    }

    public static <A, B> Iterable<Tuple2<A, B>> cartesianProduct(final Iterable<A> as, final Iterable<B> bs) {
        return CartesianProduct.<A, B>cartesianProduct(as).apply(bs);
    }
}
