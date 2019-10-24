package com.jnape.palatable.lambda.functions.builtin.fn3;

import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn3.GTWith.gtWith;
import static java.util.Comparator.comparing;
import static java.util.Comparator.naturalOrder;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class GTWithTest {
    @Test
    public void comparisons() {
        assertTrue(gtWith(naturalOrder(), 1, 2));
        assertFalse(gtWith(naturalOrder(), 1, 1));
        assertFalse(gtWith(naturalOrder(), 2, 1));

        assertTrue(gtWith(comparing(String::length), "bb", "aaa"));
    }
}