package com.jnape.palatable.lambda.lens.lenses;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.lens.Lens;

import static com.jnape.palatable.lambda.lens.Lens.simpleLens;

/**
 * Lenses that operate on {@link Either}s.
 */
public final class EitherLens {

    private EitherLens() {
    }

    /**
     * Convenience static factory method for creating a lens over right values, wrapping them in a {@link Maybe}. When
     * setting, a {@link Maybe#nothing()} value means to leave the {@link Either} unaltered, where as a
     * {@link Maybe#just} value replaces the either with a right over the {@link Maybe}.
     * <p>
     * Note that this lens is NOT lawful, since "you get back what you put in" fails for {@link Maybe#nothing()}.
     *
     * @param <L> the left parameter type
     * @param <R> the right parameter type
     * @return a lens that focuses on right values
     */
    public static <L, R> Lens.Simple<Either<L, R>, Maybe<R>> right() {
        return simpleLens(CoProduct2::projectB, (lOrR, maybeR) -> maybeR.<Either<L, R>>fmap(Either::right).orElse(lOrR));
    }

    /**
     * Convenience static factory method for creating a lens over left values, wrapping them in a {@link Maybe}. When
     * setting, a {@link Maybe#nothing()} value means to leave the {@link Either} unaltered, where as a
     * {@link Maybe#just} value replaces the either with a left over the {@link Maybe}.
     * <p>
     * Note that this lens is NOT lawful, since "you get back what you put in" fails for {@link Maybe#nothing()}.
     *
     * @param <L> the left parameter type
     * @param <R> the right parameter type
     * @return a lens that focuses on left values
     */
    public static <L, R> Lens.Simple<Either<L, R>, Maybe<L>> left() {
        return simpleLens(CoProduct2::projectA, (lOrR, maybeL) -> maybeL.<Either<L, R>>fmap(Either::left).orElse(lOrR));
    }
}
