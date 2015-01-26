package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.applicative.Functor;

@FunctionalInterface
public interface MonadicFunction<A, B> extends Functor<B> {

    B apply(A a);

    default <C> MonadicFunction<A, C> then(MonadicFunction<? super B, ? extends C> g) {
        return fmap(g);
    }

    @Override
    default <C> MonadicFunction<A, C> fmap(MonadicFunction<? super B, ? extends C> g) {
        return a -> g.apply(apply(a));
    }
}
