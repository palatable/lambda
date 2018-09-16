package com.jnape.palatable.lambda.functions.builtin.fn8;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.functions.builtin.fn8.LiftA7.liftA7;
import static org.junit.Assert.assertEquals;

public class LiftA7Test {

    @Test
    public void lifting() {
        assertEquals(just(28), liftA7((a, b, c, d, e, f, g) -> a + b + c + d + e + f + g, just(1), just(2), just(3), just(4), just(5), just(6), just(7)));
    }
}