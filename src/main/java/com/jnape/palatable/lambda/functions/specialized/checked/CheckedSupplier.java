package com.jnape.palatable.lambda.functions.specialized.checked;

import java.util.function.Supplier;

import static com.jnape.palatable.lambda.functions.specialized.checked.Runtime.throwChecked;

/**
 * Specialized {@link Supplier} that can throw checked exceptions.
 *
 * @param <E> The exception type
 * @param <T> The return type
 * @see CheckedFn1
 */
@FunctionalInterface
public interface CheckedSupplier<E extends Throwable, T> extends Supplier<T> {

    @Override
    default T get() {
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
     * @throws E any exception that can be thrown by this method
     */
    T checkedGet() throws E;
}
