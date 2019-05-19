package com.jnape.palatable.lambda.optics.prisms;

import com.jnape.palatable.lambda.optics.Prism;

import java.util.UUID;

/**
 * {@link Prism Prisms} for {@link UUID}.
 */
public final class UUIDPrism {
    private UUIDPrism() {
    }

    /**
     * A {@link Prism} that focuses on a {@link String} as a {@link UUID}.
     *
     * @return the {@link Prism}
     */
    public static Prism.Simple<String, UUID> uuid() {
        return Prism.fromPartial(UUID::fromString, UUID::toString);
    }
}
