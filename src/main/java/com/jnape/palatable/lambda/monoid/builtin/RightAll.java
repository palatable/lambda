package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.MonoidFactory;
import com.jnape.palatable.lambda.monoid.Monoid;
import com.jnape.palatable.lambda.semigroup.Semigroup;

import static com.jnape.palatable.lambda.adt.Either.right;

/**
 * A {@link Monoid} instance formed by <code>{@link Either}&lt;L,R&gt;</code> and a monoid over <code>R</code>. The
 * application to two {@link Either} values is left-biased, such that for a given {@link Either} <code>x</code> and
 * <code>y</code>:
 * <ul>
 * <li> if <code>x</code> is a <code>Left</code> value, the result is <code>x</code></li>
 * <li> if <code>y</code> is a <code>Left</code> value, the result is <code>y</code></li>
 * <li> if both <code>x</code> and <code>y</code> are right values, the result is the application of the x and y values
 * in terms of the provided monoid, wrapped in {@link Either#right}</li>
 * </ul>
 * <p>
 * For the {@link Semigroup}, see {@link com.jnape.palatable.lambda.semigroup.builtin.RightAll}.
 *
 * @param <L> The left parameter type
 * @param <R> The right parameter type
 * @see Monoid
 * @see LeftAll
 * @see Either
 */
public final class RightAll<L, R> implements MonoidFactory<Monoid<R>, Either<L, R>> {

    private RightAll() {
    }

    @Override
    public Monoid<Either<L, R>> checkedApply(Monoid<R> rMonoid) {
        Semigroup<Either<L, R>> semigroup = com.jnape.palatable.lambda.semigroup.builtin.RightAll.rightAll(rMonoid);
        return Monoid.<Either<L, R>>monoid(semigroup, () -> right(rMonoid.identity()));
    }

    public static <L, R> RightAll<L, R> rightAll() {
        return new RightAll<>();
    }

    public static <L, R> Monoid<Either<L, R>> rightAll(Monoid<R> rMonoid) {
        return RightAll.<L, R>rightAll().apply(rMonoid);
    }

    public static <L, R> Fn1<Either<L, R>, Either<L, R>> rightAll(Monoid<R> rMonoid, Either<L, R> x) {
        return RightAll.<L, R>rightAll(rMonoid).apply(x);
    }

    public static <L, R> Either<L, R> rightAll(Monoid<R> rMonoid, Either<L, R> x, Either<L, R> y) {
        return rightAll(rMonoid, x).apply(y);
    }
}
