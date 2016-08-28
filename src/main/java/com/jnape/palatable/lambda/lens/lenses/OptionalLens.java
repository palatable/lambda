package com.jnape.palatable.lambda.lens.lenses;

import com.jnape.palatable.lambda.lens.Lens;

import java.util.Optional;

import static com.jnape.palatable.lambda.lens.Lens.simpleLens;

/**
 * Lenses that operate on {@link Optional}s.
 */
public final class OptionalLens {

    private OptionalLens() {
    }

    /**
     * Convenience static factory method for creating a lens that focuses on a value as an {@link Optional}.
     *
     * @param <V> the value type
     * @return a lens that focuses on the value as an Optional
     */
    public static <V> Lens.Simple<V, Optional<V>> asOptional() {
        return simpleLens(Optional::ofNullable, (v, optV) -> optV.orElse(v));
    }
}
