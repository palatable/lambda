package com.jnape.palatable.lambda.functor.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Functor;

public class Identity<A> implements Functor<A> {

    private final A a;

    public Identity(A a) {
        this.a = a;
    }

    public A runIdentity() {
        return a;
    }

    @Override
    public <B> Identity<B> fmap(Fn1<? super A, ? extends B> fn) {
        return new Identity<>(fn.apply(a));
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Identity && a.equals(((Identity) other).a);
    }

    @Override
    public int hashCode() {
        return a.hashCode();
    }
}
