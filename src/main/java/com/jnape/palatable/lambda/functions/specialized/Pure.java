package com.jnape.palatable.lambda.functions.specialized;

import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.internal.Runtime;

/**
 * Generalized, portable {@link Applicative#pure(Object)}, with a loosened {@link Functor} constraint.
 *
 * @param <F> the {@link Functor} to lift into
 */
@FunctionalInterface
public interface Pure<F extends Functor<?, ? extends F>> {

    <A> Functor<A, ? extends F> checkedApply(A a) throws Throwable;

    default <A, FA extends Functor<A, ? extends F>> FA apply(A a) {
        try {
            @SuppressWarnings("unchecked") FA fa = (FA) checkedApply(a);
            return fa;
        } catch (Throwable t) {
            throw Runtime.throwChecked(t);
        }
    }
}
