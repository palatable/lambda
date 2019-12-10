package com.jnape.palatable.lambda.functions.builtin.fn7;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.functions.builtin.fn7.LiftA6.liftA6;
import static org.junit.Assert.assertEquals;

public class LiftA6Test {

    @Test
    public void lifting() {
        assertEquals(just(21),
                     liftA6((a, b, c, d, e, f) -> a + b + c + d + e + f,
                            just(1), just(2), just(3), just(4), just(5), just(6)));
    }
}