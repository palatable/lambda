package com.jnape.palatable.lambda.semigroup.builtin;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.SemigroupFactory;
import com.jnape.palatable.lambda.monoid.Monoid;
import com.jnape.palatable.lambda.semigroup.Semigroup;

import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;

/**
 * A {@link Semigroup} instance formed by <code>{@link Either}&lt;L,R&gt;</code> and a semigroup over <code>R</code>.
 * The application to two {@link Either} values is right-biased, such that for a given {@link Either} <code>x</code> and
 * <code>y</code>:
 * <ul>
 * <li> if both <code>x</code> and <code>y</code> are right values, the result is the application of the x and y values
 * in terms of the provided semigroup, wrapped in {@link Either#right}</li>
 * <li> if only <code>x</code> is a right value, the result is <code>x</code></li>
 * <li> if only <code>y</code> is a right value, the result is <code>y</code></li>
 * <li> if neither <code>x</code> nor <code>y</code> are right values, the result is <code>y</code></li>
 * </ul>
 * <p>
 * For the {@link Monoid}, see {@link com.jnape.palatable.lambda.monoid.builtin.RightAny}.
 *
 * @param <L> The left parameter type
 * @param <R> The right parameter type
 * @see Semigroup
 * @see Either
 */
public final class RightAny<L, R> implements SemigroupFactory<Semigroup<R>, Either<L, R>> {

    private static final RightAny<?,?> INSTANCE = new RightAny<>();

    private RightAny() {
    }

    @Override
    public Semigroup<Either<L, R>> apply(Semigroup<R> rSemigroup) {
        return (x, y) -> x.match(constantly(y),
                                 xR -> y.match(constantly(right(xR)),
                                               rSemigroup.apply(xR).andThen(Either::right)));
    }

    @SuppressWarnings("unchecked")
    public static <L, R> RightAny<L, R> rightAny() {
        return (RightAny<L, R>) INSTANCE;
    }

    public static <L, R> Semigroup<Either<L, R>> rightAny(Semigroup<R> rSemigroup) {
        return RightAny.<L, R>rightAny().apply(rSemigroup);
    }

    public static <L, R> Fn1<Either<L, R>, Either<L, R>> rightAny(Semigroup<R> rSemigroup, Either<L, R> x) {
        return RightAny.<L, R>rightAny(rSemigroup).apply(x);
    }

    public static <L, R> Either<L, R> rightAny(Semigroup<R> rSemigroup, Either<L, R> x, Either<L, R> y) {
        return rightAny(rSemigroup, x).apply(y);
    }
}
