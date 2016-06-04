package com.jnape.palatable.lambda.functions.specialized.unchecked;

import java.util.function.Supplier;

import static com.jnape.palatable.lambda.functions.specialized.Checked.throwChecked;

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
