package com.jnape.palatable.lambda.functions.builtin.fn3;

import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn3.LTWith.ltWith;
import static java.util.Comparator.comparing;
import static java.util.Comparator.naturalOrder;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LTWithTest {
    @Test
    public void comparisons() {
        assertTrue(ltWith(naturalOrder(), 2, 1));
        assertFalse(ltWith(naturalOrder(), 1, 1));
        assertFalse(ltWith(naturalOrder(), 1, 2));

        assertTrue(ltWith(comparing(String::length), "ab", "b"));
    }
}