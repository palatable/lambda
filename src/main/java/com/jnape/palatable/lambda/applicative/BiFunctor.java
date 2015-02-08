package com.jnape.palatable.lambda.applicative;

import com.jnape.palatable.lambda.functions.MonadicFunction;

import static com.jnape.palatable.lambda.functions.builtin.monadic.Identity.id;

@FunctionalInterface
public interface BiFunctor<A, B> {

    default <C> BiFunctor<C, B> biMapL(MonadicFunction<? super A, ? extends C> fn) {
        return biMap(fn, id());
    }

    default <C> BiFunctor<A, C> biMapR(MonadicFunction<? super B, ? extends C> fn) {
        return biMap(id(), fn);
    }

    <C, D> BiFunctor<C, D> biMap(MonadicFunction<? super A, ? extends C> f1,
                                 MonadicFunction<? super B, ? extends D> f2);
}
