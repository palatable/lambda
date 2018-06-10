package com.jnape.palatable.lambda.monoid;

import com.jnape.palatable.lambda.functions.Fn1;

import java.util.Collections;
import java.util.HashSet;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Distinct.distinct;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Empty.empty;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Filter.filter;
import static com.jnape.palatable.lambda.functions.builtin.fn2.ToCollection.toCollection;

public final class Difference<A> implements Monoid<Iterable<A>> {

    private static final Difference INSTANCE = new Difference();

    private Difference() {
    }

    @Override
    public Iterable<A> identity() {
        return Collections::emptyIterator;
    }


    @Override
    public Iterable<A> apply(Iterable<A> xs, Iterable<A> ys) {
        return () -> {
            if (empty(xs))
                return xs.iterator();

            if (empty(ys))
                return distinct(xs).iterator();

            //todo: pre-order dfs fold the expression tree to make stack-safe
            HashSet<A> uniqueYs = toCollection(HashSet::new, ys);
            return distinct(filter(a -> !uniqueYs.contains(a), xs)).iterator();
        };
    }

    @SuppressWarnings("unchecked")
    public static <A> Difference<A> difference() {
        return INSTANCE;
    }

    public static <A> Fn1<Iterable<A>, Iterable<A>> difference(Iterable<A> xs) {
        return Difference.<A>difference().apply(xs);
    }

    public static <A> Iterable<A> difference(Iterable<A> xs, Iterable<A> ys) {
        return difference(xs).apply(ys);
    }
}
