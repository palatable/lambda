package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.applicative.Functor;

import java.util.function.Function;

@FunctionalInterface
public interface MonadicFunction<A, B> extends Functor<B>, Function<A, B> {

    B apply(A a);

    default <C> MonadicFunction<A, C> then(MonadicFunction<? super B, ? extends C> g) {
        return fmap(g);
    }

    @Override
    default <C> MonadicFunction<A, C> fmap(MonadicFunction<? super B, ? extends C> g) {
        return a -> g.apply(apply(a));
    }
}
