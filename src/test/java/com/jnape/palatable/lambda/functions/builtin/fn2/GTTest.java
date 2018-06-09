package com.jnape.palatable.lambda.functions.builtin.fn2;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GTTest {

    @Test
    public void comparisons() {
        assertTrue(GT.gt(2, 1));
        assertFalse(GT.gt(1, 1));
        assertFalse(GT.gt(1, 2));
    }
}