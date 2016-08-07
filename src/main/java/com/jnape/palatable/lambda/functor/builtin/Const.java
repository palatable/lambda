package com.jnape.palatable.lambda.functor.builtin;

import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.functor.Functor;

import java.util.function.Function;

public final class Const<A, B> implements Functor<B>, Bifunctor<A, B> {

    private final A a;

    public Const(A a) {
        this.a = a;
    }

    public A runConst() {
        return a;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> Const<A, C> fmap(Function<? super B, ? extends C> fn) {
        return (Const<A, C>) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <Z> Const<Z, B> biMapL(Function<? super A, ? extends Z> fn) {
        return (Const<Z, B>) Bifunctor.super.biMapL(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> Const<A, C> biMapR(Function<? super B, ? extends C> fn) {
        return (Const<A, C>) Bifunctor.super.biMapR(fn);
    }

    @Override
    public <C, D> Const<C, D> biMap(Function<? super A, ? extends C> lFn,
                                    Function<? super B, ? extends D> rFn) {
        return new Const<>(lFn.apply(a));
    }
}
