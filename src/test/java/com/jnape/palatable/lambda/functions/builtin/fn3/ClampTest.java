package com.jnape.palatable.lambda.functions.builtin.fn3;

import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn3.Clamp.clamp;
import static org.junit.Assert.assertEquals;

public class ClampTest {

    @Test
    public void clampsValueBetweenBounds() {
        assertEquals((Integer) 5, clamp(1, 10, 5));
        assertEquals((Integer) 1, clamp(1, 10, -1));
        assertEquals((Integer) 10, clamp(1, 10, 11));
    }
}
