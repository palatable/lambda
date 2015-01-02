package com.jnape.palatable.lambda.functions.builtin.dyadic;

import com.jnape.palatable.lambda.functions.DyadicFunction;
import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.iterators.UnfoldingIterator;

import java.util.Optional;

import static com.jnape.palatable.lambda.tuples.Tuple2.tuple;

public final class Iterate<A> implements DyadicFunction<MonadicFunction<? super A, ? extends A>, A, Iterable<A>> {

    @Override
    public final Iterable<A> apply(final MonadicFunction<? super A, ? extends A> fn, final A seed) {
        return () -> new UnfoldingIterator<>(a -> Optional.of(tuple(a, fn.apply(a))), seed);
    }

    public static <A> Iterate<A> iterate() {
        return new Iterate<>();
    }

    public static <A> MonadicFunction<A, Iterable<A>> iterate(MonadicFunction<? super A, ? extends A> fn) {
        return Iterate.<A>iterate().apply(fn);
    }

    public static <A> Iterable<A> iterate(MonadicFunction<? super A, ? extends A> fn, A seed) {
        return iterate(fn).apply(seed);
    }
}
