package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Tupler2.tupler;
import static com.jnape.palatable.lambda.functions.builtin.fn3.ZipWith.zipWith;

/**
 * Zip together two <code>Iterable</code>s into a single <code>Iterable</code> of <code>Tuple2&lt;A, B&gt;</code>. If
 * the input <code>Iterable</code>s differ in size, the resulting <code>Iterable</code> contains only as many pairs as
 * the smallest input <code>Iterable</code>'s elements.
 *
 * @param <A> The first input Iterable element type, and the type of the first tuple slot in the output Iterable
 * @param <B> The second input Iterable element type, and the type of the second tuple slot in the output Iterable
 * @see com.jnape.palatable.lambda.functions.builtin.fn3.ZipWith
 */
public final class Zip<A, B> implements Fn2<Iterable<A>, Iterable<B>, Iterable<Tuple2<A, B>>> {

    private static final Zip<?, ?> INSTANCE = new Zip<>();

    private Zip() {
    }

    @Override
    public Iterable<Tuple2<A, B>> checkedApply(Iterable<A> as, Iterable<B> bs) {
        return zipWith(tupler(), as, bs);
    }

    @SuppressWarnings("unchecked")
    public static <A, B> Zip<A, B> zip() {
        return (Zip<A, B>) INSTANCE;
    }

    public static <A, B> Fn1<Iterable<B>, Iterable<Tuple2<A, B>>> zip(Iterable<A> as) {
        return Zip.<A, B>zip().apply(as);
    }

    public static <A, B> Iterable<Tuple2<A, B>> zip(Iterable<A> as, Iterable<B> bs) {
        return Zip.<A, B>zip(as).apply(bs);
    }
}
