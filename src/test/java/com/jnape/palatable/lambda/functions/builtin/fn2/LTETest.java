package com.jnape.palatable.lambda.functions.builtin.fn2;

import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn2.LTE.lte;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LTETest {

    @Test
    public void comparisons() {
        assertTrue(lte(1, 2));
        assertTrue(lte(1, 1));
        assertFalse(lte(2, 1));
    }
}