package com.jnape.palatable.lambda.functions.builtin.fn3;

import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn3.GTBy.gtBy;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GTByTest {

    @Test
    public void comparisons() {
        assertTrue(gtBy(id(), 2, 1));
        assertFalse(gtBy(id(), 1, 1));
        assertFalse(gtBy(id(), 1, 2));

        assertTrue(gtBy(String::length, "aaa", "bb"));
    }
}