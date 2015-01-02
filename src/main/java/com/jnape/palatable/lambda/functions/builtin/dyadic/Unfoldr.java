package com.jnape.palatable.lambda.functions.builtin.dyadic;

import com.jnape.palatable.lambda.functions.DyadicFunction;
import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.iterators.UnfoldingIterator;
import com.jnape.palatable.lambda.tuples.Tuple2;

import java.util.Optional;

public final class Unfoldr<A, B> implements DyadicFunction<MonadicFunction<B, Optional<Tuple2<A, B>>>, B, Iterable<A>> {

    @Override
    public Iterable<A> apply(MonadicFunction<B, Optional<Tuple2<A, B>>> function, B b) {
        return () -> new UnfoldingIterator<>(function, b);
    }

    public static <A, B> Unfoldr<A, B> unfoldr() {
        return new Unfoldr<>();
    }

    public static <A, B> MonadicFunction<B, Iterable<A>> unfoldr(MonadicFunction<B, Optional<Tuple2<A, B>>> function) {
        return Unfoldr.<A, B>unfoldr().apply(function);
    }

    public static <A, B> Iterable<A> unfoldr(MonadicFunction<B, Optional<Tuple2<A, B>>> function, B b) {
        return unfoldr(function).apply(b);
    }
}
