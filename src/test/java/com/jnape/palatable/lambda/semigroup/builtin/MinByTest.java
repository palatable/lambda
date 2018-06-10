package com.jnape.palatable.lambda.semigroup.builtin;

import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.semigroup.builtin.MinBy.minBy;
import static org.junit.Assert.assertEquals;

public class MinByTest {

    @Test
    public void semigroup() {
        assertEquals((Integer) 1, minBy(id(), 1, 2));
        assertEquals((Integer) 1, minBy(id(), 1, 1));
        assertEquals((Integer) 0, minBy(id(), 1, 0));

        assertEquals("a", minBy(String::length, "a", "ab"));
        assertEquals("ab", minBy(String::length, "ab", "cd"));
        assertEquals("c", minBy(String::length, "ab", "c"));
    }
}