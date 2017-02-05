package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.monoid.Monoid;

import java.util.Optional;

/**
 * A {@link Monoid} instance formed by <code>{@link Optional}&lt;A&gt;</code>. The application to two {@link Optional}
 * values produces the last non-empty value, or <code>Optional.empty()</code> if all values are empty.
 *
 * @param <A> the Optional value parameter type
 * @see First
 * @see Present
 * @see Monoid
 * @see Optional
 */
public final class Last<A> implements Monoid<Optional<A>> {
    private static final Last INSTANCE = new Last();

    private Last() {
    }

    @Override
    public Optional<A> identity() {
        return Optional.empty();
    }

    @Override
    public Optional<A> apply(Optional<A> x, Optional<A> y) {
        return y.map(Optional::of).orElse(x);
    }

    @SuppressWarnings("unchecked")
    public static <A> Last<A> last() {
        return INSTANCE;
    }

    public static <A> Fn1<Optional<A>, Optional<A>> last(Optional<A> x) {
        return Last.<A>last().apply(x);
    }

    public static <A> Optional<A> last(Optional<A> x, Optional<A> y) {
        return last(x).apply(y);
    }
}
