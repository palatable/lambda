package com.jnape.palatable.lambda.semigroup.builtin;

import org.junit.Test;

import static com.jnape.palatable.lambda.semigroup.builtin.MaxWith.maxWith;
import static java.util.Comparator.comparing;
import static java.util.Comparator.naturalOrder;
import static org.junit.Assert.assertEquals;

public class MaxWithTest {
    @Test
    public void semigroup() {
        assertEquals((Integer) 1, maxWith(naturalOrder(), 1, 0));
        assertEquals((Integer) 1, maxWith(naturalOrder(), 1, 1));
        assertEquals((Integer) 2, maxWith(naturalOrder(), 1, 2));

        assertEquals("ab", maxWith(comparing(String::length), "ab", "a"));
        assertEquals("ab", maxWith(comparing(String::length), "ab", "cd"));
        assertEquals("bc", maxWith(comparing(String::length), "a", "bc"));
    }
}