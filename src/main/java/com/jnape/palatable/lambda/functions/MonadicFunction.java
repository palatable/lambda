package com.jnape.palatable.lambda.functions;

@FunctionalInterface
public interface MonadicFunction<A, B> {

    public B apply(A a);

    public default <C> MonadicFunction<A, C> then(MonadicFunction<B, C> f) {
        return a -> f.apply(apply(a));
    }
}
