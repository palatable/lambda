package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.MonoidFactory;
import com.jnape.palatable.lambda.monoid.Monoid;
import com.jnape.palatable.lambda.semigroup.Semigroup;

import static com.jnape.palatable.lambda.adt.Either.left;

/**
 * A {@link Monoid} instance formed by <code>{@link Either}&lt;L,R&gt;</code> and a monoid over <code>L</code>. The
 * application to two {@link Either} values short-circuits on right values, such that for a given {@link Either}
 * <code>x</code> and <code>y</code>:
 * <ul>
 * <li> if <code>x</code> is a <code>Right</code> value, the result is <code>x</code></li>
 * <li> if <code>y</code> is a <code>Right</code> value, the result is <code>y</code></li>
 * <li> if both <code>x</code> and <code>y</code> are left values, the result is the application of the x and y values
 * in terms of the provided monoid, wrapped in {@link Either#left}</li>
 * </ul>
 * <p>
 * For the {@link Semigroup}, see {@link com.jnape.palatable.lambda.semigroup.builtin.LeftAll}.
 *
 * @param <L> The left parameter type
 * @param <R> The right parameter type
 * @see Monoid
 * @see RightAll
 * @see Either
 */
public final class LeftAll<L, R> implements MonoidFactory<Monoid<L>, Either<L, R>> {

    private static final LeftAll<?, ?> INSTANCE = new LeftAll<>();

    private LeftAll() {
    }

    @Override
    public Monoid<Either<L, R>> apply(Monoid<L> lMonoid) {
        Semigroup<Either<L, R>> semigroup = com.jnape.palatable.lambda.semigroup.builtin.LeftAll.leftAll(lMonoid);
        return Monoid.<Either<L, R>>monoid(semigroup, () -> left(lMonoid.identity()));
    }

    @SuppressWarnings("unchecked")
    public static <L, R> LeftAll<L, R> leftAll() {
        return (LeftAll<L, R>) INSTANCE;
    }

    public static <L, R> Monoid<Either<L, R>> leftAll(Monoid<L> lMonoid) {
        return LeftAll.<L, R>leftAll().apply(lMonoid);
    }

    public static <L, R> Fn1<Either<L, R>, Either<L, R>> leftAll(Monoid<L> lMonoid, Either<L, R> x) {
        return LeftAll.<L, R>leftAll(lMonoid).apply(x);
    }

    public static <L, R> Either<L, R> leftAll(Monoid<L> lMonoid, Either<L, R> x, Either<L, R> y) {
        return leftAll(lMonoid, x).apply(y);
    }
}
