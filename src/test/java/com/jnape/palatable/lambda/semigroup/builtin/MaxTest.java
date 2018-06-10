package com.jnape.palatable.lambda.semigroup.builtin;

import org.junit.Test;

import static com.jnape.palatable.lambda.semigroup.builtin.Max.max;
import static org.junit.Assert.assertEquals;

public class MaxTest {

    @Test
    public void semigroup() {
        assertEquals((Integer) 1, max(1, 0));
        assertEquals((Integer) 1, max(1, 1));
        assertEquals((Integer) 2, max(1, 2));
    }
}