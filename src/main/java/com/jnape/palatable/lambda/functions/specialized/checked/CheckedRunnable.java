package com.jnape.palatable.lambda.functions.specialized.checked;

import static com.jnape.palatable.lambda.functions.specialized.checked.Runtime.throwChecked;

/**
 * Specialized {@link Runnable} that can throw any {@link Throwable}.
 *
 * @param <T> The {@link Throwable} type
 */
@FunctionalInterface
public interface CheckedRunnable<T extends Throwable> extends Runnable {

    /**
     * A version of {@link Runnable#run()} that can throw checked exceptions.
     *
     * @throws T any exception that can be thrown by this method
     */
    void checkedRun() throws T;

    @Override
    default void run() {
        try {
            checkedRun();
        } catch (Throwable t) {
            throw throwChecked(t);
        }
    }

    /**
     * Convenience static factory method for constructing a {@link CheckedRunnable} without an explicit cast or type
     * attribution at the call site.
     *
     * @param runnable the checked runnable
     * @param <T>      the inferred Throwable type
     * @return the checked runnable
     */
    static <T extends Throwable> CheckedRunnable<T> checked(CheckedRunnable<T> runnable) {
        return runnable::run;
    }

}
