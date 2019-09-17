package com.jnape.palatable.lambda.functions.specialized;

import com.jnape.palatable.lambda.internal.Runtime;
import com.jnape.palatable.lambda.monad.MonadBase;
import com.jnape.palatable.lambda.monad.MonadRec;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Downcast.downcast;

/**
 * Generalized, portable lifting operation for lifting a {@link MonadRec} into a {@link MonadBase}.
 *
 * @param <B> the {@link MonadBase} to lift into
 */
@FunctionalInterface
public interface Lift<B extends MonadBase<?, ?, B>> {

    <A, M extends MonadRec<?, M>> MonadBase<M, A, B> checkedApply(MonadRec<A, M> ga)
            throws Throwable;

    default <A, M extends MonadRec<?, M>, MBA extends MonadBase<M, A, B>> MBA apply(MonadRec<A, M> ma) {
        try {
            return downcast(checkedApply(ma));
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
