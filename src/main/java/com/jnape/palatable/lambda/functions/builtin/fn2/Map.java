package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.iterators.MappingIterator;

import java.util.function.Function;

/**
 * Lazily apply a function to each element in an <code>Iterable</code>, producing an <code>Iterable</code> of the mapped
 * results.
 *
 * @param <A> A type contravariant to the input Iterable element type
 * @param <B> A type covariant to the output Iterable element type
 */
public final class Map<A, B> implements Fn2<Function<? super A, ? extends B>, Iterable<A>, Iterable<B>> {

    private Map() {
    }

    @Override
    public Iterable<B> apply(Function<? super A, ? extends B> fn, Iterable<A> as) {
        return () -> new MappingIterator<>(fn, as.iterator());
    }

    public static <A, B> Map<A, B> map() {
        return new Map<>();
    }

    public static <A, B> Fn1<Iterable<A>, Iterable<B>> map(Function<? super A, ? extends B> fn) {
        return Map.<A, B>map().apply(fn);
    }

    public static <A, B> Iterable<B> map(Function<? super A, ? extends B> fn, Iterable<A> as) {
        return Map.<A, B>map(fn).apply(as);
    }
}
