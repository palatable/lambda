package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.MonoidFactory;
import com.jnape.palatable.lambda.monoid.Monoid;
import com.jnape.palatable.lambda.semigroup.Semigroup;

import static com.jnape.palatable.lambda.adt.Either.right;

/**
 * A {@link Monoid} instance formed by <code>{@link Either}&lt;L,R&gt;</code> and a monoid over <code>R</code>. The
 * application to two {@link Either} values is right-biased, such that for a given {@link Either} <code>x</code> and
 * <code>y</code>:
 * <ul>
 * <li> if both <code>x</code> and <code>y</code> are right values, the result is the application of the x and y values
 * in terms of the provided monoid, wrapped in {@link Either#right}</li>
 * <li> if only <code>x</code> is a right value, the result is <code>x</code></li>
 * <li> if only <code>y</code> is a right value, the result is <code>y</code></li>
 * <li> if neither <code>x</code> nor <code>y</code> are right values, the result is <code>y</code></li>
 * </ul>
 * <p>
 * For the {@link Semigroup}, see {@link com.jnape.palatable.lambda.semigroup.builtin.RightAny}.
 *
 * @param <L> The left parameter type
 * @param <R> The right parameter type
 * @see Monoid
 * @see Either
 */
public final class RightAny<L, R> implements MonoidFactory<Monoid<R>, Either<L, R>> {

    private static final RightAny INSTANCE = new RightAny();

    private RightAny() {
    }

    @Override
    public Monoid<Either<L, R>> apply(Monoid<R> rMonoid) {
        Semigroup<Either<L, R>> semigroup = com.jnape.palatable.lambda.semigroup.builtin.RightAny.rightAny(rMonoid);
        return Monoid.<Either<L, R>>monoid(semigroup, () -> right(rMonoid.identity()));
    }

    @SuppressWarnings("unchecked")
    public static <L, R> RightAny<L, R> rightAny() {
        return INSTANCE;
    }

    public static <L, R> Monoid<Either<L, R>> rightAny(Monoid<R> rMonoid) {
        return RightAny.<L, R>rightAny().apply(rMonoid);
    }

    public static <L, R> Fn1<Either<L, R>, Either<L, R>> rightAny(Monoid<R> rMonoid, Either<L, R> x) {
        return RightAny.<L, R>rightAny(rMonoid).apply(x);
    }

    public static <L, R> Either<L, R> rightAny(Monoid<R> rMonoid, Either<L, R> x, Either<L, R> y) {
        return rightAny(rMonoid, x).apply(y);
    }
}
