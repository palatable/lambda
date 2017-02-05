package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.monoid.Monoid;

import java.util.Optional;

/**
 * A {@link Monoid} instance formed by <code>{@link Optional}&lt;A&gt;</code>. The application to two {@link Optional}
 * values produces the first non-empty value, or <code>Optional.empty()</code> if all values are empty.
 *
 * @param <A> the Optional value parameter type
 * @see Last
 * @see Present
 * @see Monoid
 * @see Optional
 */
public final class First<A> implements Monoid<Optional<A>> {

    private static final First INSTANCE = new First();

    private First() {
    }

    @Override
    public Optional<A> identity() {
        return Optional.empty();
    }

    @Override
    public Optional<A> apply(Optional<A> x, Optional<A> y) {
        return x.map(Optional::of).orElse(y);
    }

    @SuppressWarnings("unchecked")
    public static <A> First<A> first() {
        return INSTANCE;
    }

    public static <A> Fn1<Optional<A>, Optional<A>> first(Optional<A> x) {
        return First.<A>first().apply(x);
    }

    public static <A> Optional<A> first(Optional<A> x, Optional<A> y) {
        return first(x).apply(y);
    }
}
