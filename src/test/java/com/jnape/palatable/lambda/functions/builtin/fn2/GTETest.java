package com.jnape.palatable.lambda.functions.builtin.fn2;

import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn2.GTE.gte;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GTETest {

    @Test
    public void comparisons() {
        assertTrue(gte(1, 2));
        assertTrue(gte(1, 1));
        assertFalse(gte(2, 1));
    }
}