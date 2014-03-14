package com.jnape.palatable.lambda.exceptions;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class EmptyIterableExceptionTest {

    @Test
    public void betterNotBeChecked() {
        assertThat(RuntimeException.class.isInstance(new EmptyIterableException()), is(true));
    }

    @Test
    public void hasUsefulMessage() {
        assertThat(new EmptyIterableException().getMessage(), is("Iterable was empty."));
    }
}
