package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.MonoidFactory;
import com.jnape.palatable.lambda.monoid.Monoid;
import com.jnape.palatable.lambda.semigroup.Semigroup;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.monoid.Monoid.monoid;
import static com.jnape.palatable.lambda.monoid.builtin.First.first;

/**
 * A {@link Monoid} instance formed by <code>{@link Maybe}&lt;A&gt;</code> and a semigroup over <code>A</code>. The
 * application to two {@link Maybe} values is presence-biased, such that for a given {@link Maybe} <code>x</code>
 * and <code>y</code>:
 * <ul>
 * <li> if <code>x</code> is present and <code>y</code> is absent, the result is <code>x</code></li>
 * <li> if <code>x</code> is absent, the result is <code>y</code></li>
 * <li> if both <code>x</code> and <code>y</code> are present, the result is the application of the x and y values in
 * terms of the provided semigroup, wrapped in {@link Maybe#just}</li>
 * </ul>
 *
 * @param <A> the Maybe value parameter type
 * @see Monoid
 * @see Maybe
 */
public final class Present<A> implements MonoidFactory<Semigroup<A>, Maybe<A>> {

    private Present() {
    }

    @Override
    public Monoid<Maybe<A>> apply(Semigroup<A> aSemigroup) {
        return monoid((maybeX, maybeY) -> first(maybeX.fmap(x -> maybeY.fmap(aSemigroup.apply(x)).orElse(x)), maybeY),
                      nothing());
    }

    public static <A> Present<A> present() {
        return new Present<>();
    }

    public static <A> Monoid<Maybe<A>> present(Semigroup<A> semigroup) {
        return Present.<A>present().apply(semigroup);
    }

    public static <A> Fn1<Maybe<A>, Maybe<A>> present(Semigroup<A> aSemigroup, Maybe<A> x) {
        return present(aSemigroup).apply(x);
    }

    public static <A> Maybe<A> present(Semigroup<A> semigroup, Maybe<A> x, Maybe<A> y) {
        return present(semigroup, x).apply(y);
    }
}
