package com.jnape.palatable.lambda.functions.specialized.checked;

import com.jnape.palatable.lambda.functions.Fn1;

import static com.jnape.palatable.lambda.functions.specialized.checked.Runtime.throwChecked;

/**
 * Specialized {@link Fn1} that can throw checked exceptions.
 *
 * @param <A> The input type
 * @param <B> The output type
 * @see CheckedSupplier
 * @see Fn1
 */
@FunctionalInterface
public interface CheckedFn1<T extends Throwable, A, B> extends Fn1<A, B> {

    @Override
    default B apply(A a) {
        try {
            return checkedApply(a);
        } catch (Throwable t) {
            throw throwChecked(t);
        }
    }

    /**
     * A version of {@link Fn1} that can throw checked exceptions.
     *
     * @param a the argument
     * @return the result of the function application
     * @throws T any Throwable thrown by the function application
     */
    B checkedApply(A a) throws T;
}
