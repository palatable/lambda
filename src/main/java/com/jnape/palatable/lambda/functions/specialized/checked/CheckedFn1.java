package com.jnape.palatable.lambda.functions.specialized.checked;

import com.jnape.palatable.lambda.functions.Fn1;

import static com.jnape.palatable.lambda.functions.specialized.checked.Runtime.throwChecked;

/**
 * Specialized {@link Fn1} that can throw any {@link Throwable}.
 *
 * @param <T> The {@link Throwable} type
 * @param <A> The input type
 * @param <B> The output type
 * @see CheckedSupplier
 * @see CheckedRunnable
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
     * A version of {@link Fn1#apply} that can throw checked exceptions.
     *
     * @param a the function argument
     * @return the application of the argument to the function
     * @throws T any exception that can be thrown by this method
     */
    B checkedApply(A a) throws T;

    /**
     * Convenience static factory method for constructing a {@link CheckedFn1} without an explicit cast or type
     * attribution at the call site.
     *
     * @param checkedFn1 the checked fn1
     * @param <T>        the inferred Throwable type
     * @param <A>        the input type
     * @param <B>        the output type
     * @return the checked fn1
     */
    static <T extends Throwable, A, B> CheckedFn1<T, A, B> checked(CheckedFn1<T, A, B> checkedFn1) {
        return checkedFn1;
    }
}
