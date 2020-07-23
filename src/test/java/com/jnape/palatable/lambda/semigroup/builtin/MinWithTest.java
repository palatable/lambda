package com.jnape.palatable.lambda.semigroup.builtin;

import org.junit.Test;

import static com.jnape.palatable.lambda.semigroup.builtin.MinWith.minWith;
import static java.util.Comparator.comparing;
import static java.util.Comparator.naturalOrder;
import static org.junit.Assert.assertEquals;

public class MinWithTest {
    @Test
    public void semigroup() {
        assertEquals((Integer) 1, minWith(naturalOrder(), 1, 2));
        assertEquals((Integer) 1, minWith(naturalOrder(), 1, 1));
        assertEquals((Integer) 0, minWith(naturalOrder(), 1, 0));

        assertEquals("a", minWith(comparing(String::length), "a", "ab"));
        assertEquals("ab", minWith(comparing(String::length), "ab", "cd"));
        assertEquals("c", minWith(comparing(String::length), "ab", "c"));
    }
}