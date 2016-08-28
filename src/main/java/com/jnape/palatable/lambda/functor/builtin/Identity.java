package com.jnape.palatable.lambda.functor.builtin;

import com.jnape.palatable.lambda.functor.Functor;

import java.util.function.Function;

/**
 * A functor over some value of type <code>A</code> that can be mapped over and retrieved later.
 *
 * @param <A> the value type
 */
public final class Identity<A> implements Functor<A> {

    private final A a;

    public Identity(A a) {
        this.a = a;
    }

    /**
     * Retrieve the value.
     *
     * @return the value
     */
    public A runIdentity() {
        return a;
    }

    /**
     * Covariantly map over the value.
     *
     * @param fn  the mapping function
     * @param <B> the new value type
     * @return an Identity over B (the new value)
     */
    @Override
    public <B> Identity<B> fmap(Function<? super A, ? extends B> fn) {
        return new Identity<>(fn.apply(a));
    }
}
