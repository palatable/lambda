package com.jnape.palatable.lambda.recursionschemes;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functor.Functor;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;

public interface Cofree<A, F extends Functor, CofreeF extends Functor<?, F>> {

    Tuple2<A, CofreeF> uncofree();

    static <A, F extends Functor, CofreeF extends Functor<? extends Cofree<A, F, ?>, F>> Cofree<A, F, CofreeF> coFree(
            A a, CofreeF f) {
        return () -> tuple(a, f);
    }
}
