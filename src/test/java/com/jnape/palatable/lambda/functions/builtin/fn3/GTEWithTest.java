package com.jnape.palatable.lambda.functions.builtin.fn3;

import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn3.GTEWith.gteWith;
import static java.util.Comparator.comparing;
import static java.util.Comparator.naturalOrder;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GTEWithTest {
    @Test
    public void comparisons() {
        assertTrue(gteWith(naturalOrder(), 1, 2));
        assertTrue(gteWith(naturalOrder(), 1, 1));
        assertFalse(gteWith(naturalOrder(), 2, 1));

        assertTrue(gteWith(comparing(String::length), "b", "ab"));
        assertTrue(gteWith(comparing(String::length), "bc", "ab"));
    }
}