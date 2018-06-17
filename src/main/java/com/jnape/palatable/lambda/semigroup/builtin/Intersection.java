package com.jnape.palatable.lambda.semigroup.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn1.Distinct;
import com.jnape.palatable.lambda.semigroup.Semigroup;

import java.util.HashSet;
import java.util.Set;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Distinct.distinct;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Eq.eq;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Filter.filter;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Find.find;

/**
 * Given two {@link Iterable Iterables} <code>xs</code> and <code>ys</code>, return the {@link Distinct distinct}
 * elements of <code>xs</code> that are also in <code>ys</code> in order of their unique occurrence in <code>xs</code>.
 *
 * @param <A> the {@link Iterable} element type
 */
public final class Intersection<A> implements Semigroup<Iterable<A>> {

    private static final Intersection INSTANCE = new Intersection();

    private Intersection() {
    }

    @Override
    public Iterable<A> apply(Iterable<A> xs, Iterable<A> ys) {
        Set<A> seen = new HashSet<>();
        return distinct(filter(a -> seen.contains(a) || find(eq(a), ys).peek(seen::add).fmap(constantly(true)).orElse(false), xs));
    }

    @SuppressWarnings("unchecked")
    public static <A> Intersection<A> intersection() {
        return INSTANCE;
    }

    public static <A> Fn1<Iterable<A>, Iterable<A>> intersection(Iterable<A> xs) {
        return Intersection.<A>intersection().apply(xs);
    }

    public static <A> Iterable<A> intersection(Iterable<A> xs, Iterable<A> ys) {
        return intersection(xs).apply(ys);
    }
}
