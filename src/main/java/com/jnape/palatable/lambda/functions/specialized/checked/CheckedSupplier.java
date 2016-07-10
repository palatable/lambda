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
public interface CheckedSupplier<E extends Exception, T> extends Supplier<T> {

    @Override
    default T get() {
        try {
            return checkedGet();
        } catch (Throwable t) {
            throw throwChecked(t);
        }
    }

    T checkedGet() throws E;
}
