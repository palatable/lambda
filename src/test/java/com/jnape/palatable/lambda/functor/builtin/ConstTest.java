package com.jnape.palatable.lambda.functor.builtin;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConstTest {

    @Test
    public void functorProperties() {
        assertEquals("foo", new Const<String, Integer>("foo").fmap(x -> x + 1).runConst());
    }

    @Test
    public void bifunctorProperties() {
        assertEquals("FOO", new Const<String, Integer>("foo").biMap(String::toUpperCase, x -> x + 1).runConst());
    }
}