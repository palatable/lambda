package com.jnape.palatable.lambda.semigroup.builtin;

import org.junit.Test;

import static com.jnape.palatable.lambda.semigroup.builtin.Min.min;
import static org.junit.Assert.assertEquals;

public class MinTest {

    @Test
    public void semigroup() {
        assertEquals((Integer) 1, min(1, 2));
        assertEquals((Integer) 1, min(1, 1));
        assertEquals((Integer) 0, min(1, 0));
    }
}