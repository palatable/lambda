package com.jnape.palatable.lambda.functions;

@FunctionalInterface
public interface MonadicFunction<A, B> {

    B apply(A a);

    default <C> MonadicFunction<A, C> then(MonadicFunction<B, C> f) {
        return a -> f.apply(apply(a));
    }
}
