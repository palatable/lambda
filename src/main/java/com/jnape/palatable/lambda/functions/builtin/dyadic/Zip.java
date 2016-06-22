package com.jnape.palatable.lambda.functions.builtin.dyadic;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.DyadicFunction;
import com.jnape.palatable.lambda.functions.MonadicFunction;

import static com.jnape.palatable.lambda.functions.builtin.dyadic.Tupler2.tupler;
import static com.jnape.palatable.lambda.functions.builtin.triadic.ZipWith.zipWith;

/**
 * Zip together two <code>Iterable</code>s into a single <code>Iterable</code> of <code>Tuple2&lt;A, B&gt;</code>. If
 * the input <code>Iterable</code>s differ in size, the resulting <code>Iterable</code> contains only as many pairs as
 * the smallest input <code>Iterable</code>'s elements.
 *
 * @param <A> The first input Iterable element type, and the type of the first tuple slot in the output Iterable
 * @param <B> The second input Iterable element type, and the type of the second tuple slot in the output Iterable
 * @see com.jnape.palatable.lambda.functions.builtin.triadic.ZipWith
 */
public final class Zip<A, B> implements DyadicFunction<Iterable<A>, Iterable<B>, Iterable<Tuple2<A, B>>> {

    private Zip() {
    }

    @Override
    public Iterable<Tuple2<A, B>> apply(Iterable<A> as, Iterable<B> bs) {
        return zipWith(tupler(), as, bs);
    }

    public static <A, B> Zip<A, B> zip() {
        return new Zip<>();
    }

    public static <A, B> MonadicFunction<Iterable<B>, Iterable<Tuple2<A, B>>> zip(Iterable<A> as) {
        return Zip.<A, B>zip().apply(as);
    }

    public static <A, B> Iterable<Tuple2<A, B>> zip(Iterable<A> as, Iterable<B> bs) {
        return Zip.<A, B>zip(as).apply(bs);
    }
}
