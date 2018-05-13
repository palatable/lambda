package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Init.init;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Tails.tails;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Take.take;
import static com.jnape.palatable.lambda.functions.builtin.fn3.Times.times;

/**
 * Given an <code>{@link Iterable}&lt;A&gt;</code>, "slide" a window of <code>k</code> elements across the {@link
 * Iterable} by one element at a time, returning an <code>{@link Iterable}&lt;{@link Iterable}&lt;A&gt;&gt;</code>.
 * <p>
 * Example:
 *
 * <code>slide(2, asList(1, 2, 3, 4, 5)); // [[1, 2], [2, 3], [3, 4], [4, 5]]</code>
 *
 * @param <A> the Iterable element type
 */
public final class Slide<A> implements Fn2<Integer, Iterable<A>, Iterable<Iterable<A>>> {

    private static final Slide INSTANCE = new Slide<>();

    private Slide() {
    }

    @Override
    public Iterable<Iterable<A>> apply(Integer k, Iterable<A> as) {
        if (k == 0)
            throw new IllegalArgumentException("k must be greater than 0");

        return times(k, init(), map(take(k), tails(as)));
    }

    @SuppressWarnings("unchecked")
    public static <A> Slide<A> slide() {
        return INSTANCE;
    }

    public static <A> Fn1<Iterable<A>, Iterable<Iterable<A>>> slide(Integer k) {
        return Slide.<A>slide().apply(k);
    }

    public static <A> Iterable<Iterable<A>> slide(Integer k, Iterable<A> as) {
        return Slide.<A>slide(k).apply(as);
    }
}
