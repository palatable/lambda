package com.jnape.palatable.lambda.exceptions;

public class EmptyIterableException extends RuntimeException {

    public EmptyIterableException() {
        super("Iterable was empty.");
    }
}
