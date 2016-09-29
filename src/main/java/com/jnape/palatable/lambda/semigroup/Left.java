package com.jnape.palatable.lambda.semigroup;

import com.jnape.palatable.lambda.adt.Either;

import static com.jnape.palatable.lambda.adt.Either.left;

/**
 * The {@link Semigroup} formed by <code>{@link Either}&lt;L,R&gt;</code> and a <code>Semigroup</code> over
 * <code>L</code>. The application to two {@link Either} values is right-biased, such that for a given {@link
 * Either} <code>x</code> and <code>y</code>:
 * <ul>
 * <li> if <code>x</code> is a <code>Right</code> value, the result is <code>x</code></li>
 * <li> if <code>y</code> is a <code>Right</code> value, the result is <code>y</code></li>
 * <li> if both <code>x</code> and <code>y</code> are <code>Left</code> values, the result is the application of the x
 * and y values in terms of the provided semigroup, wrapped in a <code>Left</code></li>
 * </ul>
 *
 * @param <L> the left parameter type
 * @param <R> the right parameter type
 */
public final class Left<L, R> implements Semigroup<Either<L, R>> {

    private final Semigroup<L> semigroup;

    public Left(Semigroup<L> semigroup) {
        this.semigroup = semigroup;
    }

    @Override
    public Either<L, R> apply(Either<L, R> x, Either<L, R> y) {
        return x.flatMap(xL -> y.flatMap(yL -> left(semigroup.apply(xL, yL)),
                                         Either::right),
                         Either::right);
    }
}
