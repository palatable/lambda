package com.jnape.palatable.lambda.functions.specialized.checked;

import static com.jnape.palatable.lambda.functions.specialized.checked.Runtime.throwChecked;

/**
 * Specialized {@link Runnable} that can throw any {@link Throwable}.
 *
 * @param <T> The {@link Throwable} type
 * @see CheckedSupplier
 * @see CheckedFn1
 */
@FunctionalInterface
public interface CheckedRunnable<T extends Throwable> extends Runnable {

    @Override
    default void run() {
        try {
            checkedRun();
        } catch (Throwable t) {
            throw throwChecked(t);
        }
    }

    void checkedRun() throws T;
}
