package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.MonoidFactory;
import com.jnape.palatable.lambda.monoid.Monoid;
import com.jnape.palatable.lambda.semigroup.Semigroup;

import static com.jnape.palatable.lambda.adt.Either.left;

/**
 * A {@link Monoid} instance formed by <code>{@link Either}&lt;L,R&gt;</code> and a monoid over <code>L</code>. The
 * application to two {@link Either} values is left-biased, such that for a given {@link Either} <code>x</code> and
 * <code>y</code>:
 * <ul>
 * <li> if both <code>x</code> and <code>y</code> are left values, the result is the application of the x and y values
 * in terms of the provided monoid, wrapped in {@link Either#left}</li>
 * <li> if only <code>x</code> is a left value, the result is <code>x</code></li>
 * <li> if only <code>y</code> is a left value, the result is <code>y</code></li>
 * <li> if neither <code>x</code> nor <code>y</code> are left values, the result is <code>y</code></li>
 * </ul>
 * <p>
 * For the {@link Semigroup}, see {@link com.jnape.palatable.lambda.semigroup.builtin.LeftAny}.
 *
 * @param <L> The left parameter type
 * @param <R> The right parameter type
 * @see Monoid
 * @see Either
 */
public final class LeftAny<L, R> implements MonoidFactory<Monoid<L>, Either<L, R>> {

    private static final LeftAny<?,?> INSTANCE = new LeftAny<>();

    private LeftAny() {
    }

    @Override
    public Monoid<Either<L, R>> apply(Monoid<L> lMonoid) {
        Semigroup<Either<L, R>> semigroup = com.jnape.palatable.lambda.semigroup.builtin.LeftAny.leftAny(lMonoid);
        return Monoid.<Either<L, R>>monoid(semigroup, () -> left(lMonoid.identity()));
    }

    @SuppressWarnings("unchecked")
    public static <L, R> LeftAny<L, R> leftAny() {
        return (LeftAny<L, R>) INSTANCE;
    }

    public static <L, R> Monoid<Either<L, R>> leftAny(Monoid<L> lMonoid) {
        return LeftAny.<L, R>leftAny().apply(lMonoid);
    }

    public static <L, R> Fn1<Either<L, R>, Either<L, R>> leftAny(Monoid<L> lMonoid, Either<L, R> x) {
        return LeftAny.<L, R>leftAny(lMonoid).apply(x);
    }

    public static <L, R> Either<L, R> leftAny(Monoid<L> lMonoid, Either<L, R> x, Either<L, R> y) {
        return leftAny(lMonoid, x).apply(y);
    }
}
