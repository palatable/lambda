package com.jnape.palatable.lambda.functor.builtin;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IdentityTest {

    @Test
    public void functorProperties() {
        assertEquals("FOO", new Identity<>("foo").fmap(String::toUpperCase).runIdentity());
    }
}