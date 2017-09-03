package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.functions.Fn1;

import java.util.HashMap;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Filter.filter;

/**
 * Return an {@link Iterable} of the distinct values from the given input {@link Iterable}.
 *
 * @param <A> the Iterable element type
 */
public final class Distinct<A> implements Fn1<Iterable<A>, Iterable<A>> {
    private static final Distinct INSTANCE = new Distinct();

    private Distinct() {
    }

    @Override
    public Iterable<A> apply(Iterable<A> as) {
        HashMap<A, Boolean> known = new HashMap<>();
        return filter(a -> known.putIfAbsent(a, true) == null, as);
    }

    @SuppressWarnings("unchecked")
    public static <A> Distinct<A> distinct() {
        return INSTANCE;
    }

    public static <A> Iterable<A> distinct(Iterable<A> as) {
        return Distinct.<A>distinct().apply(as);
    }
}
