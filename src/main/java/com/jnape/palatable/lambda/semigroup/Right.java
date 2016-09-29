package com.jnape.palatable.lambda.semigroup;

import com.jnape.palatable.lambda.adt.Either;

import static com.jnape.palatable.lambda.adt.Either.right;

/**
 * The {@link Semigroup} formed by <code>{@link Either}&lt;L,R&gt;</code> and a <code>Semigroup</code> over
 * <code>R</code>. The application to two {@link Either} values is left-biased, such that for a given {@link
 * Either} <code>x</code> and <code>y</code>:
 * <ul>
 * <li> if <code>x</code> is a <code>Left</code> value, the result is <code>x</code></li>
 * <li> if <code>y</code> is a <code>Left</code> value, the result is <code>y</code></li>
 * <li> if both <code>x</code> and <code>y</code> are <code>right</code> values, the result is the application of the x
 * and y values in terms of the provided semigroup, wrapped in a <code>Right</code></li>
 * </ul>
 *
 * @param <L> the left parameter type
 * @param <R> the right parameter type
 */
public final class Right<L, R> implements Semigroup<Either<L, R>> {

    private final Semigroup<R> semigroup;

    public Right(Semigroup<R> semigroup) {
        this.semigroup = semigroup;
    }

    @Override
    public Either<L, R> apply(Either<L, R> eitherX, Either<L, R> eitherY) {
        return eitherX.flatMap(xR -> eitherY.flatMap(yR -> right(semigroup.apply(xR, yR))));
    }
}
