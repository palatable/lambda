package com.jnape.palatable.lambda.functions.builtin.fn3;

import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn3.GTEBy.gteBy;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GTEByTest {

    @Test
    public void comparisons() {
        assertTrue(gteBy(id(), 2, 1));
        assertTrue(gteBy(id(), 1, 1));
        assertFalse(gteBy(id(), 1, 2));

        assertTrue(gteBy(String::length, "ab", "b"));
        assertTrue(gteBy(String::length, "ab", "bc"));
    }
}