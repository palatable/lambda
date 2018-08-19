package com.jnape.palatable.lambda.functions.specialized.checked;

import com.jnape.palatable.lambda.functions.Effect;

import static com.jnape.palatable.lambda.functions.specialized.checked.Runtime.throwChecked;

/**
 * Specialized {@link Effect} that can throw any {@link Throwable}.
 *
 * @param <T> The {@link Throwable} type
 * @param <A> The input type
 * @see CheckedRunnable
 * @see CheckedFn1
 * @see Effect
 */
@FunctionalInterface
public interface CheckedEffect<T extends Throwable, A> extends Effect<A> {

    @Override
    default void accept(A a) {
        try {
            checkedAccept(a);
        } catch (Throwable t) {
            throw throwChecked(t);
        }
    }

    /**
     * A version of {@link Effect#accept} that can throw checked exceptions.
     *
     * @param a the effect argument
     * @throws T any exception that can be thrown by this method
     */
    void checkedAccept(A a) throws T;

    /**
     * Convenience static factory method for constructing a {@link CheckedEffect} without an explicit cast or type
     * attribution at the call site.
     *
     * @param checkedEffect the checked effect
     * @param <T>           the inferred Throwable type
     * @param <A>           the input type
     * @return the checked effect
     */
    static <T extends Throwable, A> CheckedEffect<T, A> checked(CheckedEffect<T, A> checkedEffect) {
        return checkedEffect;
    }
}
