package com.jnape.palatable.lambda.semigroup.builtin;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.SemigroupFactory;
import com.jnape.palatable.lambda.monoid.Monoid;
import com.jnape.palatable.lambda.semigroup.Semigroup;

import static com.jnape.palatable.lambda.adt.Either.left;

/**
 * A {@link Semigroup} instance formed by <code>{@link Either}&lt;L,R&gt;</code> and a semigroup over <code>L</code>.
 * The application to two {@link Either} values is left-biased, such that for a given {@link Either} <code>x</code> and
 * <code>y</code>:
 * <ul>
 * <li> if both <code>x</code> and <code>y</code> are left values, the result is the application of the x and y values
 * in terms of the provided semigroup, wrapped in {@link Either#left}</li>
 * <li> if only <code>x</code> is a left value, the result is <code>x</code></li>
 * <li> if only <code>y</code> is a left value, the result is <code>y</code></li>
 * <li> if neither <code>x</code> nor <code>y</code> are left values, the result is <code>y</code></li>
 * </ul>
 * <p>
 * For the {@link Monoid}, see {@link com.jnape.palatable.lambda.monoid.builtin.LeftAny}.
 *
 * @param <L> The left parameter type
 * @param <R> The right parameter type
 * @see Semigroup
 * @see Either
 */
public final class LeftAny<L, R> implements SemigroupFactory<Semigroup<L>, Either<L, R>> {

    private static final LeftAny INSTANCE = new LeftAny();

    private LeftAny() {
    }

    @Override
    public Semigroup<Either<L, R>> apply(Semigroup<L> lSemigroup) {
        return (x, y) -> x.flatMap(xL -> y.flatMap(yL -> left(lSemigroup.apply(xL, yL)),
                                                   yR -> left(xL)),
                                   xR -> y);
    }

    @SuppressWarnings("unchecked")
    public static <L, R> LeftAny<L, R> leftAny() {
        return INSTANCE;
    }

    public static <L, R> Semigroup<Either<L, R>> leftAny(Semigroup<L> lSemigroup) {
        return LeftAny.<L, R>leftAny().apply(lSemigroup);
    }

    public static <L, R> Fn1<Either<L, R>, Either<L, R>> leftAny(Semigroup<L> lSemigroup, Either<L, R> x) {
        return LeftAny.<L, R>leftAny(lSemigroup).apply(x);
    }

    public static <L, R> Either<L, R> leftAny(Semigroup<L> lSemigroup, Either<L, R> x, Either<L, R> y) {
        return leftAny(lSemigroup, x).apply(y);
    }
}
