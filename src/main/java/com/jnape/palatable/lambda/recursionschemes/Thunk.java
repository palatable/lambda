package com.jnape.palatable.lambda.recursionschemes;

import com.jnape.palatable.lambda.functor.Functor;

import java.util.function.Function;
import java.util.function.Supplier;

public final class Thunk<A, F extends Functor> implements Functor<A, Thunk<?, F>>, Supplier<Functor<A, F>> {
    private final Supplier<Functor<A, F>> supplier;

    public Thunk(Supplier<Functor<A, F>> supplier) {
        this.supplier = supplier;
    }

    @Override
    public <B> Thunk<B, F> fmap(Function<? super A, ? extends B> fn) {
        return new Thunk<>(() -> supplier.get().fmap(fn));
    }

    @Override
    public Functor<A, F> get() {
        return supplier.get();
    }
}
