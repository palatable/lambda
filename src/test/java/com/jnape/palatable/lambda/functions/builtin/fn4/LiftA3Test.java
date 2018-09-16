package com.jnape.palatable.lambda.functions.builtin.fn4;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.functions.builtin.fn4.LiftA3.liftA3;
import static org.junit.Assert.assertEquals;

public class LiftA3Test {

    @Test
    public void lifting() {
        assertEquals(just(6), liftA3((a, b, c) -> a + b + c, just(1), just(2), just(3)));
    }
}