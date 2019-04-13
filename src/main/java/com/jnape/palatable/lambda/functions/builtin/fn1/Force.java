package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.functions.Fn1;

/**
 * Force a full iteration of an {@link Iterable}, presumably to perform any side-effects contained therein. Returns the
 * {@link Iterable} back.
 *
 * @param <A> the Iterable element type
 */
public final class Force<A> implements Fn1<Iterable<A>, Iterable<A>> {

    private static final Force<?> INSTANCE = new Force<>();

    private Force() {
    }

    @Override
    @SuppressWarnings("StatementWithEmptyBody")
    public Iterable<A> apply(Iterable<A> as) {
        for (A ignored : as) {
        }
        return as;
    }

    @SuppressWarnings("unchecked")
    public static <A> Force<A> force() {
        return (Force<A>) INSTANCE;
    }

    public static <A> Iterable<A> force(Iterable<A> as) {
        return Force.<A>force().apply(as);
    }
}
