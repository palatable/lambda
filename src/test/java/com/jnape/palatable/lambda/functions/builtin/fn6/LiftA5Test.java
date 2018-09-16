package com.jnape.palatable.lambda.functions.builtin.fn6;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.functions.builtin.fn6.LiftA5.liftA5;
import static org.junit.Assert.assertEquals;

public class LiftA5Test {

    @Test
    public void lifting() {
        assertEquals(just(15), liftA5((a, b, c, d, e) -> a + b + c + d + e, just(1), just(2), just(3), just(4), just(5)));
    }
}