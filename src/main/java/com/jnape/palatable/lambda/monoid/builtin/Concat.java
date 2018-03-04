package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.iteration.ConcatenatingIterable;
import com.jnape.palatable.lambda.monoid.Monoid;

import java.util.Collections;

/**
 * The {@link Monoid} instance formed under concatenation for an arbitrary {@link Iterable}.
 *
 * @see Monoid
 */
public final class Concat<A> implements Monoid<Iterable<A>> {

    private static final Concat INSTANCE = new Concat();

    private Concat() {
    }

    @Override
    public Iterable<A> identity() {
        return Collections::emptyIterator;
    }

    @Override
    public Iterable<A> apply(Iterable<A> xs, Iterable<A> ys) {
        return new ConcatenatingIterable<>(xs, ys);
    }

    @SuppressWarnings("unchecked")
    public static <A> Concat<A> concat() {
        return INSTANCE;
    }

    public static <A> Fn1<Iterable<A>, Iterable<A>> concat(Iterable<A> xs) {
        return Concat.<A>concat().apply(xs);
    }

    public static <A> Iterable<A> concat(Iterable<A> xs, Iterable<A> ys) {
        return concat(xs).apply(ys);
    }
}
