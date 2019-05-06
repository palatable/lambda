package com.jnape.palatable.lambda.functions.specialized;

import com.jnape.palatable.lambda.functions.specialized.checked.Runtime;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Functor;

/**
 * Generalized, portable {@link Applicative#pure(Object)}, with a loosened {@link Functor} constraint.
 *
 * @param <F> the {@link Functor} to lift into
 */
@FunctionalInterface
public interface Pure<F extends Functor<?, ? extends F>> {

    <A> Functor<A, ? extends F> checkedApply(A a) throws Throwable;

    default <A> Functor<A, ? extends F> apply(A a) {
        try {
            return checkedApply(a);
        } catch (Throwable t) {
            throw Runtime.throwChecked(t);
        }
    }
}
