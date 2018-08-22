package com.jnape.palatable.lambda.functions.builtin.fn3;

import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn3.LTBy.ltBy;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LTByTest {

    @Test
    public void comparisons() {
        assertTrue(ltBy(id(), 2, 1));
        assertFalse(ltBy(id(), 1, 1));
        assertFalse(ltBy(id(), 1, 2));

        assertTrue(ltBy(String::length, "ab", "b"));
    }
}