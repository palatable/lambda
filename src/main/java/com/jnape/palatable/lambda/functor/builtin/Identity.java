package com.jnape.palatable.lambda.functor.builtin;

import com.jnape.palatable.lambda.functor.Functor;

import java.util.function.Function;

public class Identity<A> implements Functor<A> {

    private final A a;

    public Identity(A a) {
        this.a = a;
    }

    public A runIdentity() {
        return a;
    }

    @Override
    public <B> Identity<B> fmap(Function<? super A, ? extends B> fn) {
        return new Identity<>(fn.apply(a));
    }
}
