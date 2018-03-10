package com.jnape.palatable.lambda.functions.specialized.checked;

import java.util.function.Supplier;

import static com.jnape.palatable.lambda.functions.specialized.checked.Runtime.throwChecked;

/**
 * Specialized {@link Supplier} that can throw any {@link Throwable}.
 *
 * @param <T> The {@link Throwable} type
 * @param <A> The return type
 * @see CheckedFn1
 * @see CheckedRunnable
 */
@FunctionalInterface
public interface CheckedSupplier<T extends Throwable, A> extends Supplier<A> {

    @Override
    default A get() {
        try {
            return checkedGet();
        } catch (Throwable t) {
            throw throwChecked(t);
        }
    }

    /**
     * A version of {@link Supplier#get()} that can throw checked exceptions.
     *
     * @return the supplied result
     * @throws T any exception that can be thrown by this method
     */
    A checkedGet() throws T;

    /**
     * Convert this {@link CheckedSupplier} to a {@link CheckedRunnable}.
     *
     * @return the checked runnable
     */
    default CheckedRunnable<T> toRunnable() {
        return this::get;
    }

    /**
     * Convenience static factory method for constructing a {@link CheckedSupplier} without an explicit cast or type
     * attribution at the call site.
     *
     * @param supplier the checked supplier
     * @param <T>      the inferred Throwable type
     * @param <A>      the supplier return type
     * @return the checked supplier
     */
    static <T extends Throwable, A> CheckedSupplier<T, A> checked(CheckedSupplier<T, A> supplier) {
        return supplier::get;
    }
}
