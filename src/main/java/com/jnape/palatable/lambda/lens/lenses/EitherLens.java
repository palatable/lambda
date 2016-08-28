package com.jnape.palatable.lambda.lens.lenses;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.lens.Lens;

import java.util.Optional;

import static com.jnape.palatable.lambda.lens.Lens.simpleLens;

/**
 * Lenses that operate on {@link Either}s.
 */
public final class EitherLens {

    private EitherLens() {
    }

    /**
     * Convenience static factory method for creating a lens over right values, wrapping them in an {@link Optional}.
     * When setting, an empty Optional value means to leave the either unaltered, where as a present Optional value
     * replaces the either with a right over the wrapped Optional value.
     *
     * @param <L> the left parameter type
     * @param <R> the right parameter type
     * @return a lens that focuses on right values
     */
    public static <L, R> Lens.Simple<Either<L, R>, Optional<R>> right() {
        return simpleLens(Either::toOptional, (lOrR, optR) -> optR.<Either<L, R>>map(Either::right).orElse(lOrR));
    }

    /**
     * Convenience static factory method for creating a lens over left values, wrapping them in an {@link Optional}.
     * When setting, an empty Optional value means to leave the either unaltered, where as a present Optional value
     * replaces the either with a left over the wrapped Optional value.
     *
     * @param <L> the left parameter type
     * @param <R> the right parameter type
     * @return a lens that focuses on left values
     */
    public static <L, R> Lens.Simple<Either<L, R>, Optional<L>> left() {
        return simpleLens(e -> e.match(Optional::ofNullable, __ -> Optional.empty()),
                          (lOrR, optL) -> optL.<Either<L, R>>map(Either::left).orElse(lOrR));
    }
}
