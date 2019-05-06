package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.builtin.fn1.Distinct;

import java.util.HashSet;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Distinct.distinct;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Empty.empty;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Filter.filter;
import static com.jnape.palatable.lambda.functions.builtin.fn2.ToCollection.toCollection;

/**
 * Given two {@link Iterable Iterables} <code>xs</code> and <code>ys</code>, return the {@link Distinct distinct}
 * elements of <code>xs</code> that are not in <code>ys</code>. Note that this is <strong>not</strong> symmetric
 * difference.
 * <p>
 * This operation preserves order, so the resulting elements from <code>xs</code> are iterated in the order that
 * they uniquely occur in.
 *
 * @param <A> the {@link Iterable} element type
 */
public final class Difference<A> implements Fn2<Iterable<A>, Iterable<A>, Iterable<A>> {

    private static final Difference<?> INSTANCE = new Difference<>();

    private Difference() {
    }

    @Override
    public Iterable<A> checkedApply(Iterable<A> xs, Iterable<A> ys) {
        return () -> {
            if (empty(xs))
                return xs.iterator();

            if (empty(ys))
                return distinct(xs).iterator();

            //todo: a pre-order depth-first fold of the expression tree would make this stack-safe
            HashSet<A> uniqueYs = toCollection(HashSet::new, ys);
            return distinct(filter(a -> !uniqueYs.contains(a), xs)).iterator();
        };
    }

    @SuppressWarnings("unchecked")
    public static <A> Difference<A> difference() {
        return (Difference<A>) INSTANCE;
    }

    public static <A> Fn1<Iterable<A>, Iterable<A>> difference(Iterable<A> xs) {
        return Difference.<A>difference().apply(xs);
    }

    public static <A> Iterable<A> difference(Iterable<A> xs, Iterable<A> ys) {
        return difference(xs).apply(ys);
    }
}
