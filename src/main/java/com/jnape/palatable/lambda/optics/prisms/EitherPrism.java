package com.jnape.palatable.lambda.optics.prisms;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.optics.Prism;

import static com.jnape.palatable.lambda.optics.Prism.simplePrism;

/**
 * {@link Prism Prisms} for {@link Either}.
 */
public final class EitherPrism {

    private EitherPrism() {
    }

    /**
     * A {@link Prism} that focuses on the {@link Either#left(Object) left} value of an {@link Either}.
     *
     * @param <L> the left type
     * @param <R> the right type
     * @return the {@link Prism}
     */
    public static <L, R> Prism.Simple<Either<L, R>, L> _left() {
        return simplePrism(CoProduct2::projectA, Either::left);
    }

    /**
     * A {@link Prism} that focuses on the {@link Either#right(Object) right} value of an {@link Either}.
     *
     * @param <L> the left type
     * @param <R> the right type
     * @return the {@link Prism}
     */
    public static <L, R> Prism.Simple<Either<L, R>, R> _right() {
        return simplePrism(CoProduct2::projectB, Either::right);
    }
}
