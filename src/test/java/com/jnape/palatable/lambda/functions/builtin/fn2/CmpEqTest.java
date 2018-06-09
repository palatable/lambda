package com.jnape.palatable.lambda.functions.builtin.fn2;

import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn2.CmpEq.cmpEq;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CmpEqTest {

    @Test
    public void comparisons() {
        assertTrue(cmpEq(1, 1));
        assertFalse(cmpEq(1, 2));
        assertFalse(cmpEq(2, 1));
    }
}