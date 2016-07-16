package com.jnape.palatable.lambda.lens.lenses;

import com.jnape.palatable.lambda.lens.Lens;

import java.util.Optional;

import static com.jnape.palatable.lambda.lens.Lens.simpleLens;

public final class OptionalLens {

    private OptionalLens() {
    }

    public static <V> Lens.Simple<V, Optional<V>> asOptional() {
        return simpleLens(Optional::ofNullable, (v, optV) -> optV.orElse(v));
    }
}
