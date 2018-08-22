package com.jnape.palatable.lambda.functions.builtin.fn2;

import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn2.GT.gt;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GTTest {

    @Test
    public void comparisons() {
        assertTrue(gt(1, 2));
        assertFalse(gt(1, 1));
        assertFalse(gt(2, 1));
    }
}