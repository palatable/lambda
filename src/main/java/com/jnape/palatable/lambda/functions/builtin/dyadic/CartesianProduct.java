package com.jnape.palatable.lambda.functions.builtin.dyadic;

import com.jnape.palatable.lambda.adt.tuples.Tuple2;
import com.jnape.palatable.lambda.functions.DyadicFunction;
import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.iterators.CombinatorialIterator;

/**
 * Lazily compute the cartesian product of an <code>Iterable&lt;A&gt;</code> and <code>Iterable&lt;B&gt;</code>,
 * returning an <code>Iterable&lt;Tuple2&lt;A, B&gt;&gt;</code>, the <em>products</em> as tuples of
 * <em>multiplicand</em> <code>A</code>s and <em>multiplier</em> <code>B</code>s.
 * <p></p>
 * Note that this algorithm exhaustively pairs all elements from <code>Iterable&lt;B&gt;</code> to the first element of
 * <code>Iterable&lt;A&gt;</code> before advancing to the next element of <code>Iterable&lt;A&gt;</code>, <strong>so if
 * <code>Iterable&lt;B&gt;</code> is infinite, only one element from <code>Iterable&lt;A&gt;</code> will ever be
 * paired</strong>.
 *
 * @param <A> The multiplicand Iterable element type
 * @param <B> The multiplier Iterable element type
 * @see Zip
 */
public final class CartesianProduct<A, B> implements DyadicFunction<Iterable<A>, Iterable<B>, Iterable<Tuple2<A, B>>> {

    private CartesianProduct() {
    }

    @Override
    public final Iterable<Tuple2<A, B>> apply(Iterable<A> as, Iterable<B> bs) {
        return () -> new CombinatorialIterator<>(as.iterator(), bs.iterator());
    }

    public static <A, B> CartesianProduct<A, B> cartesianProduct() {
        return new CartesianProduct<>();
    }

    public static <A, B> MonadicFunction<Iterable<B>, Iterable<Tuple2<A, B>>> cartesianProduct(Iterable<A> as) {
        return CartesianProduct.<A, B>cartesianProduct().apply(as);
    }

    public static <A, B> Iterable<Tuple2<A, B>> cartesianProduct(Iterable<A> as, Iterable<B> bs) {
        return CartesianProduct.<A, B>cartesianProduct(as).apply(bs);
    }
}
