package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Repeat.repeat;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Take.take;

/**
 * Produce an {@link Iterable} of a value <code>n</code> times.
 *
 * @param <A> the output Iterable element type
 */
public final class Replicate<A> implements Fn2<Integer, A, Iterable<A>> {

    private static final Replicate INSTANCE = new Replicate();

    private Replicate() {
    }

    @Override
    public Iterable<A> apply(Integer n, A a) {
        return take(n, repeat(a));
    }

    @SuppressWarnings("unchecked")
    public static <A> Replicate<A> replicate() {
        return INSTANCE;
    }

    public static <A> Fn1<A, Iterable<A>> replicate(Integer n) {
        return Replicate.<A>replicate().apply(n);
    }

    public static <A> Iterable<A> replicate(Integer n, A a) {
        return Replicate.<A>replicate(n).apply(a);
    }
}
