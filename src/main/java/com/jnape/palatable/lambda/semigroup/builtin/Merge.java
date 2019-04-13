package com.jnape.palatable.lambda.semigroup.builtin;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.BiSemigroupFactory;
import com.jnape.palatable.lambda.functions.specialized.SemigroupFactory;
import com.jnape.palatable.lambda.monoid.Monoid;
import com.jnape.palatable.lambda.semigroup.Semigroup;

/**
 * A {@link Semigroup} instance formed by {@link Either#merge} and semigroups over <code>L</code> and <code>R</code>.
 * Like {@link Either#merge}, this is left-biased.
 * <p>
 * For the {@link Monoid}, see {@link com.jnape.palatable.lambda.monoid.builtin.Merge}.
 *
 * @param <L> The left parameter type
 * @param <R> The right parameter type
 * @see Monoid
 * @see Either#merge
 */
public final class Merge<L, R> implements BiSemigroupFactory<Semigroup<L>, Semigroup<R>, Either<L, R>> {

    private static final Merge<?, ?> INSTANCE = new Merge<>();

    private Merge() {
    }

    @Override
    public Semigroup<Either<L, R>> apply(Semigroup<L> lSemigroup, Semigroup<R> rSemigroup) {
        return (x, y) -> x.merge(lSemigroup::apply, rSemigroup::apply, y);
    }

    @SuppressWarnings("unchecked")
    public static <L, R> Merge<L, R> merge() {
        return (Merge<L, R>) INSTANCE;
    }

    public static <L, R> SemigroupFactory<Semigroup<R>, Either<L, R>> merge(Semigroup<L> lSemigroup) {
        return Merge.<L, R>merge().apply(lSemigroup);
    }

    public static <L, R> Semigroup<Either<L, R>> merge(Semigroup<L> lSemigroup, Semigroup<R> rSemigroup) {
        return Merge.<L, R>merge(lSemigroup).apply(rSemigroup);
    }

    public static <L, R> Fn1<Either<L, R>, Either<L, R>> merge(Semigroup<L> lSemigroup, Semigroup<R> rSemigroup,
                                                               Either<L, R> x) {
        return merge(lSemigroup, rSemigroup).apply(x);
    }

    public static <L, R> Either<L, R> merge(Semigroup<L> lSemigroup, Semigroup<R> rSemigroup, Either<L, R> x,
                                            Either<L, R> y) {
        return merge(lSemigroup, rSemigroup, x).apply(y);
    }
}
