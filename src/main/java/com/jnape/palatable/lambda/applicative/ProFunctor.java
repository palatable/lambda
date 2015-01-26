package com.jnape.palatable.lambda.applicative;

import com.jnape.palatable.lambda.functions.MonadicFunction;

import static com.jnape.palatable.lambda.functions.builtin.monadic.Identity.id;

public interface ProFunctor<A, B> {

    default <C> ProFunctor<C, B> diMapL(MonadicFunction<? super C, ? extends A> fn) {
        return diMap(fn, id());
    }

    default <C> ProFunctor<A, C> diMapR(MonadicFunction<? super B, ? extends C> fn) {
        return diMap(id(), fn);
    }

    <C, D> ProFunctor<C, D> diMap(MonadicFunction<? super C, ? extends A> f1,
                                  MonadicFunction<? super B, ? extends D> f2);
}
