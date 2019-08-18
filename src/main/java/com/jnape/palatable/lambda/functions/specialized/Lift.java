package com.jnape.palatable.lambda.functions.specialized;

import com.jnape.palatable.lambda.internal.Runtime;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.monad.MonadBase;

/**
 * Generalized, portable lifting operation for lifting a {@link Monad} into a {@link MonadBase}.
 *
 * @param <B> the {@link MonadBase} to lift into
 */
@FunctionalInterface
public interface Lift<B extends MonadBase<?, ?, B>> {

    <A, M extends Monad<?, M>> MonadBase<M, A, B> checkedApply(Monad<A, M> ga)
            throws Throwable;

    default <A, M extends Monad<?, M>, MBA extends MonadBase<M, A, B>> MBA apply(Monad<A, M> ma) {
        try {
            @SuppressWarnings("unchecked") MBA MBA = (MBA) checkedApply(ma);
            return MBA;
        } catch (Throwable t) {
            throw Runtime.throwChecked(t);
        }
    }

    /**
     * Static method to aid inference.
     *
     * @param lift the {@link Lift}
     * @param <B>  the {@link MonadBase} to lift into
     * @return the {@link Lift}
     */
    static <B extends MonadBase<?, ?, B>> Lift<B> lift(Lift<B> lift) {
        return lift;
    }
}
