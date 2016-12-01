package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.MonoidFactory;
import com.jnape.palatable.lambda.monoid.Monoid;
import com.jnape.palatable.lambda.semigroup.Semigroup;

import java.util.Optional;
import java.util.function.Function;

import static com.jnape.palatable.lambda.monoid.Monoid.monoid;

/**
 * A {@link Monoid} instance formed by <code>{@link Optional}&lt;A&gt;</code> and a semigroup over <code>A</code>. The
 * application to two {@link Optional} values is present-biased, such that for a given {@link Optional} <code>x</code>
 * and <code>y</code>:
 * <ul>
 * <li> if <code>x</code> is a present value and <code>y</code> is empty, the result is <code>x</code></li>
 * <li> if <code>x</code> is an empty value and <code>y</code> is present, the result is <code>y</code></li>
 * <li> if both <code>x</code> and <code>y</code> are present, the result is the application of the x and y values in
 * terms of the provided semigroup, wrapped in a present <code>Optional</code></li>
 * <li> if both <code>x</code> and <code>y</code> are empty, the result is empty</li>
 * </ul>
 *
 * @param <A> the Optional value parameter type
 * @see Monoid
 * @see Optional
 */
public final class Present<A> implements MonoidFactory<Semigroup<A>, Optional<A>> {

    private Present() {
    }

    @Override
    public Monoid<Optional<A>> apply(Semigroup<A> aSemigroup) {
        Semigroup<Optional<A>> semigroup = (optX, optY) -> {
            Function<A, Optional<A>> combine = x -> Optional.of(optY.map(aSemigroup.apply(x)).orElse(x));
            return optX.map(combine).orElse(optY);
        };
        return monoid(semigroup, Optional.empty());
    }

    public static <A> Present<A> present() {
        return new Present<>();
    }

    public static <A> Monoid<Optional<A>> present(Semigroup<A> semigroup) {
        return Present.<A>present().apply(semigroup);
    }

    public static <A> Fn1<Optional<A>, Optional<A>> present(Semigroup<A> aSemigroup, Optional<A> x) {
        return present(aSemigroup).apply(x);
    }

    public static <A> Optional<A> present(Semigroup<A> semigroup, Optional<A> x, Optional<A> y) {
        return present(semigroup, x).apply(y);
    }
}
