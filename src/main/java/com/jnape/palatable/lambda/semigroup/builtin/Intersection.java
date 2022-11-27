package com.jnape.palatable.lambda.semigroup.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn1.Distinct;
import com.jnape.palatable.lambda.functions.recursion.RecursiveResult;
import com.jnape.palatable.lambda.semigroup.ShortCircuitingSemigroup;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Distinct.distinct;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Empty.empty;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Eq.eq;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Filter.filter;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Find.find;
import static com.jnape.palatable.lambda.functions.recursion.RecursiveResult.recurse;
import static com.jnape.palatable.lambda.functions.recursion.RecursiveResult.terminate;
import static java.util.Collections.emptyList;

/**
 * Given two {@link Iterable Iterables} <code>xs</code> and <code>ys</code>, return the {@link Distinct distinct}
 * elements of <code>xs</code> that are also in <code>ys</code> in order of their unique occurrence in <code>xs</code>.
 *
 * @param <A> the {@link Iterable} element type
 */
public final class Intersection<A> implements ShortCircuitingSemigroup<Iterable<A>> {

    private static final Intersection<?> INSTANCE = new Intersection<>();

    private Intersection() {
    }

    @SuppressWarnings("unchecked")
    public static <A> Intersection<A> intersection() {
        return (Intersection<A>) INSTANCE;
    }

    public static <A> Fn1<Iterable<A>, Iterable<A>> intersection(Iterable<A> xs) {
        return Intersection.<A>intersection().apply(xs);
    }

    public static <A> Iterable<A> intersection(Iterable<A> xs, Iterable<A> ys) {
        return intersection(xs).apply(ys);
    }

    @Override
    public RecursiveResult<Iterable<A>, Iterable<A>> shortCircuitApply(Iterable<A> a1, Iterable<A> a2) {
        if (empty(a1) || empty(a2))
            return terminate(emptyList());
        return recurse(filter(x -> find(eq(x), a1).fmap(constantly(true)).orElse(false), distinct(a2)));
    }

    @Override
    public Iterable<A> checkedApply(Iterable<A> xs, Iterable<A> ys) {
        return filter(x -> find(eq(x), ys).fmap(constantly(true)).orElse(false), distinct(xs));
    }
}
