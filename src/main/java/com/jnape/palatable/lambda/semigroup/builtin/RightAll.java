package com.jnape.palatable.lambda.semigroup.builtin;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.SemigroupFactory;
import com.jnape.palatable.lambda.monoid.Monoid;
import com.jnape.palatable.lambda.semigroup.Semigroup;

import static com.jnape.palatable.lambda.adt.Either.right;

/**
 * A {@link Semigroup} instance formed by <code>{@link Either}&lt;L,R&gt;</code> and a semigroup over <code>R</code>.
 * The application to two {@link Either} values is left-biased, such that for a given {@link Either} <code>x</code> and
 * <code>y</code>:
 * <ul>
 * <li> if <code>x</code> is a <code>Left</code> value, the result is <code>x</code></li>
 * <li> if <code>y</code> is a <code>Left</code> value, the result is <code>y</code></li>
 * <li> if both <code>x</code> and <code>y</code> are right values, the result is the application of the x and y values
 * in terms of the provided semigroup, wrapped in {@link Either#right}</li>
 * </ul>
 * <p>
 * For the {@link Monoid}, see {@link com.jnape.palatable.lambda.monoid.builtin.RightAll}.
 *
 * @param <L> The left parameter type
 * @param <R> The right parameter type
 * @see Semigroup
 * @see LeftAll
 * @see Either
 */
public final class RightAll<L, R> implements SemigroupFactory<Semigroup<R>, Either<L, R>> {

    private static final RightAll<?, ?> INSTANCE = new RightAll<>();

    private RightAll() {
    }

    @Override
    public Semigroup<Either<L, R>> checkedApply(Semigroup<R> rSemigroup) {
        return (eitherX, eitherY) -> eitherX.flatMap(xR -> eitherY.flatMap(yR -> right(rSemigroup.apply(xR, yR))));
    }

    @SuppressWarnings("unchecked")
    public static <L, R> RightAll<L, R> rightAll() {
        return (RightAll<L, R>) INSTANCE;
    }

    public static <L, R> Semigroup<Either<L, R>> rightAll(Semigroup<R> rSemigroup) {
        return RightAll.<L, R>rightAll().apply(rSemigroup);
    }

    public static <L, R> Fn1<Either<L, R>, Either<L, R>> rightAll(Semigroup<R> rSemigroup, Either<L, R> x) {
        return RightAll.<L, R>rightAll(rSemigroup).apply(x);
    }

    public static <L, R> Either<L, R> rightAll(Semigroup<R> rSemigroup, Either<L, R> x, Either<L, R> y) {
        return rightAll(rSemigroup, x).apply(y);
    }
}
