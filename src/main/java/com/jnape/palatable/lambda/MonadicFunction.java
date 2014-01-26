package com.jnape.palatable.lambda;

public abstract class MonadicFunction<Input, Output> {
    public abstract Output apply(Input input);
}
