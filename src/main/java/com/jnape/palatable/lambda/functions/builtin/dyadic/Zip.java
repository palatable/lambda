package com.jnape.palatable.lambda.functions.builtin.dyadic;

import com.jnape.palatable.lambda.adt.tuples.Tuple2;
import com.jnape.palatable.lambda.functions.DyadicFunction;
import com.jnape.palatable.lambda.functions.MonadicFunction;

import static com.jnape.palatable.lambda.functions.builtin.triadic.ZipWith.zipWith;

public final class Zip<A, B> implements DyadicFunction<Iterable<A>, Iterable<B>, Iterable<Tuple2<A, B>>> {

    @Override
    public final Iterable<Tuple2<A, B>> apply(final Iterable<A> as, final Iterable<B> bs) {
        return zipWith(Tupler2.<A, B>tupler(), as, bs);
    }

    public static <A, B> Zip<A, B> zip() {
        return new Zip<>();
    }

    public static <A, B> MonadicFunction<Iterable<B>, Iterable<Tuple2<A, B>>> zip(Iterable<A> as) {
        return Zip.<A, B>zip().apply(as);
    }

    public static <A, B> Iterable<Tuple2<A, B>> zip(Iterable<A> as, Iterable<B> bs) {
        return Zip.<A, B>zip().apply(as, bs);
    }
}
