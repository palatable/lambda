package com.jnape.palatable.lambda.functions.specialized;

import com.jnape.palatable.lambda.functions.specialized.checked.Runtime;
import com.jnape.palatable.lambda.io.IO;

/**
 * An interface used to represent an effect that requires no input and produces no output, and therefore is only
 * perceivable through inspection of some unreported state. Only exists because Java target-type inference requires an
 * interface, or else this would all be internal, hence the inconveniently-named <code>Ω</code>.
 * <p>
 * <code>Ω</code> should *never* be called directly.
 *
 * @see IO#io(SideEffect)
 */
public interface SideEffect {

    @SuppressWarnings("NonAsciiCharacters")
    void Ω() throws Throwable;

    /**
     * Convert this {@link SideEffect} to a java {@link Runnable}.
     *
     * @return the {@link Runnable}
     */
    default Runnable toRunnable() {
        return () -> {
            try {
                Ω();
            } catch (Throwable t) {
                throw Runtime.throwChecked(t);
            }
        };
    }

    /**
     * Create a {@link SideEffect} from a java {@link Runnable}.
     *
     * @param runnable the {@link Runnable}
     * @return the {@link SideEffect}
     */
    static SideEffect fromRunnable(Runnable runnable) {
        return runnable::run;
    }

    /**
     * Static factory method to aid in inference.
     *
     * @param sideEffect the {@link SideEffect}
     * @return the {@link SideEffect}
     */
    static SideEffect sideEffect(SideEffect sideEffect) {
        return sideEffect;
    }
}
