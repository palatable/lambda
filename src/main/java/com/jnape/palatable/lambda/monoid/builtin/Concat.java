package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.iteration.ConcatenatingIterable;
import com.jnape.palatable.lambda.monoid.Monoid;

import java.util.Collections;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Flatten.flatten;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;

/**
 * The {@link Monoid} instance formed under concatenation for an arbitrary {@link Iterable}.
 *
 * @see Monoid
 */
public final class Concat<A> implements Monoid<Iterable<A>> {

    private static final Concat<?> INSTANCE = new Concat<>();

    private Concat() {
    }

    @Override
    public Iterable<A> identity() {
        return Collections::emptyIterator;
    }

    @Override
    public Iterable<A> checkedApply(Iterable<A> xs, Iterable<A> ys) {
        return new ConcatenatingIterable<>(xs, ys);
    }

    @Override
    public <B> Iterable<A> foldMap(Fn1<? super B, ? extends Iterable<A>> fn, Iterable<B> bs) {
        return flatten(map(fn, bs));
    }

    @SuppressWarnings("unchecked")
    public static <A> Concat<A> concat() {
        return (Concat<A>) INSTANCE;
    }

    public static <A> Fn1<Iterable<A>, Iterable<A>> concat(Iterable<A> xs) {
        return Concat.<A>concat().apply(xs);
    }

    public static <A> Iterable<A> concat(Iterable<A> xs, Iterable<A> ys) {
        return concat(xs).apply(ys);
    }
}
