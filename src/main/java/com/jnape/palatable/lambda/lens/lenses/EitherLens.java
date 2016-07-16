package com.jnape.palatable.lambda.lens.lenses;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.lens.Lens;

import java.util.Optional;

import static com.jnape.palatable.lambda.lens.Lens.simpleLens;

public final class EitherLens {

    private EitherLens() {
    }

    public static <L, R> Lens.Simple<Either<L, R>, Optional<R>> right() {
        return simpleLens(Either::toOptional, (lOrR, optR) -> optR.<Either<L, R>>map(Either::right).orElse(lOrR));
    }

    public static <L, R> Lens.Simple<Either<L, R>, Optional<L>> left() {
        return simpleLens(e -> e.match(Optional::ofNullable, __ -> Optional.empty()),
                          (lOrR, optL) -> optL.<Either<L, R>>map(Either::left).orElse(lOrR));
    }
}
