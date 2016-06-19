package com.jnape.palatable.lambda.functions.builtin.dyadic;

import com.jnape.palatable.lambda.functions.DyadicFunction;
import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.iterators.MappingIterator;

/**
 * Lazily apply a function to each element in an <code>Iterable</code>, producing an <code>Iterable</code> of the mapped
 * results.
 *
 * @param <A> A type contravariant to the input Iterable element type
 * @param <B> A type covariant to the output Iterable element type
 */
public final class Map<A, B> implements DyadicFunction<MonadicFunction<? super A, ? extends B>, Iterable<A>, Iterable<B>> {

    private Map() {
    }

    @Override
    public Iterable<B> apply(MonadicFunction<? super A, ? extends B> function, Iterable<A> as) {
        return () -> new MappingIterator<>(function, as.iterator());
    }

    public static <A, B> Map<A, B> map() {
        return new Map<>();
    }

    public static <A, B> MonadicFunction<Iterable<A>, Iterable<B>> map(
            MonadicFunction<? super A, ? extends B> function) {
        return Map.<A, B>map().apply(function);
    }

    public static <A, B> Iterable<B> map(MonadicFunction<? super A, ? extends B> function, Iterable<A> as) {
        return Map.<A, B>map(function).apply(as);
    }
}
