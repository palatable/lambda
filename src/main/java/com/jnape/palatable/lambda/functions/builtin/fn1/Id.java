package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.functions.Fn1;

/**
 * The identity function.
 *
 * @param <A> The input/output type
 */
public final class Id<A> implements Fn1<A, A> {

    private static final Id<?> INSTANCE = new Id<>();

    private Id() {
    }

    @Override
    public A checkedApply(A a) {
        return a;
    }

    @SuppressWarnings("unchecked")
    public static <A> Id<A> id() {
        return (Id<A>) INSTANCE;
    }

    public static <A> A id(A a) {
        return Id.<A>id().apply(a);
    }
}
