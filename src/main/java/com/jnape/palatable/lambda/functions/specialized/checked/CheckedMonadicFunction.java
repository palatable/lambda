package com.jnape.palatable.lambda.functions.specialized.checked;

import com.jnape.palatable.lambda.functions.MonadicFunction;

import static com.jnape.palatable.lambda.functions.specialized.checked.Runtime.throwChecked;

/**
 * Specialized {@link MonadicFunction} that can throw checked exceptions.
 *
 * @param <A> The input type
 * @param <B> The output type
 * @see CheckedSupplier
 * @see MonadicFunction
 */
@FunctionalInterface
public interface CheckedMonadicFunction<A, B> extends MonadicFunction<A, B> {

    @Override
    default B apply(A a) {
        try {
            return checkedApply(a);
        } catch (Throwable t) {
            throw throwChecked(t);
        }
    }

    B checkedApply(A a) throws Exception;
}
