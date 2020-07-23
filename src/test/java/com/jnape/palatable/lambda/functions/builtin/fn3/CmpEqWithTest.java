package com.jnape.palatable.lambda.functions.builtin.fn3;

import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn3.CmpEqBy.cmpEqBy;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CmpEqWithTest {
    @Test
    public void comparisons() {
        assertTrue(cmpEqBy(id(), 1, 1));
        assertFalse(cmpEqBy(id(), 1, 2));
        assertFalse(cmpEqBy(id(), 2, 1));

        assertTrue(cmpEqBy(String::length, "b", "a"));
    }
}