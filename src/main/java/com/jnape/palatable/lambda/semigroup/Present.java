package com.jnape.palatable.lambda.semigroup;

import com.jnape.palatable.lambda.monoid.Monoid;

import java.util.Optional;

/**
 * The {@link Semigroup} formed by <code>{@link Optional}&lt;A&gt;</code> and a <code>Semigroup</code> over
 * <code>A</code>. The application to two {@link Optional} values is empty-biased, such that for a given {@link
 * Optional} <code>x</code> and <code>y</code>:
 * <ul>
 * <li> if <code>x</code> is <code>Optional.empty()</code> value, the result is <code>x</code></li>
 * <li> if <code>y</code> is <code>Optional.empty()</code> value, the result is <code>y</code></li>
 * <li> if both <code>x</code> and <code>y</code> are present, the result is the application of the x and y values in
 * terms of the provided semigroup, wrapped in a present <code>Optional</code></li>
 * </ul>
 *
 * @param <A> the Optional value parameter type
 */
public final class Present<A> implements Monoid<Optional<A>> {

    private Semigroup<A> semigroup;

    public Present(Semigroup<A> semigroup) {
        this.semigroup = semigroup;
    }

    @Override
    public Optional<A> identity() {
        return Optional.empty();
    }

    @Override
    public Optional<A> apply(Optional<A> optX, Optional<A> optY) {
        return optX.flatMap(x -> optY.flatMap(y -> Optional.of(semigroup.apply(x, y))));
    }
}
