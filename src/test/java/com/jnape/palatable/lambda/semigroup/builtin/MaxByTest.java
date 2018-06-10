package com.jnape.palatable.lambda.semigroup.builtin;

import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.semigroup.builtin.MaxBy.maxBy;
import static org.junit.Assert.assertEquals;

public class MaxByTest {

    @Test
    public void semigroup() {
        assertEquals((Integer) 1, maxBy(id(), 1, 0));
        assertEquals((Integer) 1, maxBy(id(), 1, 1));
        assertEquals((Integer) 2, maxBy(id(), 1, 2));

        assertEquals("ab", maxBy(String::length, "ab", "a"));
        assertEquals("ab", maxBy(String::length, "ab", "cd"));
        assertEquals("bc", maxBy(String::length, "a", "bc"));
    }
}