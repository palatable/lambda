package com.jnape.palatable.lambda.functions.specialized.checked;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.functions.IO;

import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static com.jnape.palatable.lambda.functions.specialized.checked.Runtime.throwChecked;

/**
 * Specialized {@link Runnable} that can throw any {@link Throwable}.
 *
 * @param <T> The {@link Throwable} type
 * @see CheckedSupplier
 * @see CheckedFn1
 */
@FunctionalInterface
public interface CheckedRunnable<T extends Throwable> extends Runnable, IO<Unit> {

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

    @Override
    default Unit unsafePerformIO() {
        run();
        return UNIT;
    }

    /**
     * Convert this {@link CheckedRunnable} to a {@link CheckedSupplier} that returns {@link Unit}.
     *
     * @return the checked supplier
     */
    default CheckedSupplier<T, Unit> toSupplier() {
        return () -> {
            run();
            return UNIT;
        };
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
