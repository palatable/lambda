package com.jnape.palatable.lambda.functions.builtin.monadic;

import com.jnape.palatable.lambda.functions.MonadicFunction;

/**
 * The identity function.
 *
 * @param <A> The input/output type
 */
public final class Id<A> implements MonadicFunction<A, A> {

    private static final Id ID = new Id();

    private Id() {
    }

    @Override
    public A apply(A a) {
        return a;
    }

    @SuppressWarnings("unchecked")
    public static <A> Id<A> id() {
        return (Id<A>) ID;
    }
}
