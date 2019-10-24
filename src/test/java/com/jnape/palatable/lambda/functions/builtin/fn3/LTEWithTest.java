package com.jnape.palatable.lambda.functions.builtin.fn3;

import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn3.LTEWith.lteWith;
import static java.util.Comparator.comparing;
import static java.util.Comparator.naturalOrder;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LTEWithTest {
    @Test
    public void comparisons() {
        assertTrue(lteWith(naturalOrder(), 2, 1));
        assertTrue(lteWith(naturalOrder(), 1, 1));
        assertFalse(lteWith(naturalOrder(), 1, 2));

        assertTrue(lteWith(comparing(String::length), "ab", "b"));
        assertTrue(lteWith(comparing(String::length), "ab", "bc"));
    }
}