package com.jnape.palatable.lambda;

public abstract class MonadicFunction<A, B> {
    public abstract B apply(A a);
}
