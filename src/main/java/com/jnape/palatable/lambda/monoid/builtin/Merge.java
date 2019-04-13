package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.BiMonoidFactory;
import com.jnape.palatable.lambda.functions.specialized.MonoidFactory;
import com.jnape.palatable.lambda.monoid.Monoid;
import com.jnape.palatable.lambda.semigroup.Semigroup;

import static com.jnape.palatable.lambda.adt.Either.right;

/**
 * A {@link Monoid} instance formed by {@link Either#merge}, a semigroup over <code>L</code>, and a monoid over
 * <code>R</code>. Like {@link Either#merge}, this is left-biased.
 * <p>
 * For the {@link Semigroup}, see {@link com.jnape.palatable.lambda.semigroup.builtin.Merge}.
 *
 * @param <L> The left parameter type
 * @param <R> The right parameter type
 * @see Monoid
 * @see Either#merge
 */
public final class Merge<L, R> implements BiMonoidFactory<Semigroup<L>, Monoid<R>, Either<L, R>> {

    private static final Merge<?,?> INSTANCE = new Merge<>();

    private Merge() {
    }

    @Override
    public Monoid<Either<L, R>> apply(Semigroup<L> lSemigroup, Monoid<R> rMonoid) {
        Semigroup<Either<L, R>> semigroup = com.jnape.palatable.lambda.semigroup.builtin.Merge.merge(lSemigroup, rMonoid);
        return Monoid.<Either<L, R>>monoid(semigroup, () -> right(rMonoid.identity()));
    }

    @SuppressWarnings("unchecked")
    public static <L, R> Merge<L, R> merge() {
        return (Merge<L, R>) INSTANCE;
    }

    public static <L, R> MonoidFactory<Monoid<R>, Either<L, R>> merge(Semigroup<L> lSemigroup) {
        return Merge.<L, R>merge().apply(lSemigroup);
    }

    public static <L, R> Monoid<Either<L, R>> merge(Semigroup<L> lSemigroup, Monoid<R> rMonoid) {
        return Merge.<L, R>merge(lSemigroup).apply(rMonoid);
    }

    public static <L, R> Fn1<Either<L, R>, Either<L, R>> merge(Semigroup<L> lSemigroup, Monoid<R> rMonoid,
                                                               Either<L, R> x) {
        return merge(lSemigroup, rMonoid).apply(x);
    }

    public static <L, R> Either<L, R> merge(Semigroup<L> lSemigroup, Monoid<R> rMonoid, Either<L, R> x,
                                            Either<L, R> y) {
        return merge(lSemigroup, rMonoid, x).apply(y);
    }
}
