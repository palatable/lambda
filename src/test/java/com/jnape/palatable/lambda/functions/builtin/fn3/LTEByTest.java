package com.jnape.palatable.lambda.functions.builtin.fn3;

import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn3.LTEBy.lteBy;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LTEByTest {

    @Test
    public void comparisons() {
        assertTrue(lteBy(id(), 2, 1));
        assertTrue(lteBy(id(), 1, 1));
        assertFalse(lteBy(id(), 1, 2));

        assertTrue(lteBy(String::length, "ab", "b"));
        assertTrue(lteBy(String::length, "ab", "bc"));
    }
}