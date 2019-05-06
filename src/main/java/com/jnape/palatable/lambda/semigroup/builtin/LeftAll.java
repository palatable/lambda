package com.jnape.palatable.lambda.semigroup.builtin;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.SemigroupFactory;
import com.jnape.palatable.lambda.monoid.Monoid;
import com.jnape.palatable.lambda.monoid.builtin.RightAll;
import com.jnape.palatable.lambda.semigroup.Semigroup;

import static com.jnape.palatable.lambda.adt.Either.left;

/**
 * A {@link Semigroup} instance formed by <code>{@link Either}&lt;L,R&gt;</code> and a semigroup over <code>L</code>.
 * The application to two {@link Either} values is right-biased, such that for a given {@link Either} <code>x</code> and
 * <code>y</code>:
 * <ul>
 * <li> if <code>x</code> is a <code>Right</code> value, the result is <code>x</code></li>
 * <li> if <code>y</code> is a <code>Right</code> value, the result is <code>y</code></li>
 * <li> if both <code>x</code> and <code>y</code> are left values, the result is the application of the x and y values
 * in terms of the provided semigroup, wrapped in {@link Either#left}</li>
 * </ul>
 * <p>
 * For the {@link Monoid}, see {@link com.jnape.palatable.lambda.monoid.builtin.LeftAll}.
 *
 * @param <L> The left parameter type
 * @param <R> The right parameter type
 * @see Semigroup
 * @see RightAll
 * @see Either
 */
public final class LeftAll<L, R> implements SemigroupFactory<Semigroup<L>, Either<L, R>> {

    private static final LeftAll<?, ?> INSTANCE = new LeftAll<>();

    private LeftAll() {
    }

    @Override
    public Semigroup<Either<L, R>> checkedApply(Semigroup<L> lSemigroup) {
        return (x, y) -> x.match(xL -> y.match(yL -> left(lSemigroup.apply(xL, yL)), Either::right), Either::right);
    }

    @SuppressWarnings("unchecked")
    public static <L, R> LeftAll<L, R> leftAll() {
        return (LeftAll<L, R>) INSTANCE;
    }

    public static <L, R> Semigroup<Either<L, R>> leftAll(Semigroup<L> lSemigroup) {
        return LeftAll.<L, R>leftAll().apply(lSemigroup);
    }

    public static <L, R> Fn1<Either<L, R>, Either<L, R>> leftAll(Semigroup<L> lSemigroup, Either<L, R> x) {
        return LeftAll.<L, R>leftAll(lSemigroup).apply(x);
    }

    public static <L, R> Either<L, R> leftAll(Semigroup<L> lSemigroup, Either<L, R> x, Either<L, R> y) {
        return leftAll(lSemigroup, x).apply(y);
    }
}
