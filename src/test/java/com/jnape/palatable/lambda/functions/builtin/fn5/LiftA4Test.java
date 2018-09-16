package com.jnape.palatable.lambda.functions.builtin.fn5;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.functions.builtin.fn5.LiftA4.liftA4;
import static org.junit.Assert.assertEquals;

public class LiftA4Test {

    @Test
    public void lifting() {
        assertEquals(just(10), liftA4((a, b, c, d) -> a + b + c + d, just(1), just(2), just(3), just(4)));
    }
}