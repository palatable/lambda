package com.jnape.palatable.lambda.functions.builtin.fn3;

import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn3.Between.between;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BetweenTest {

    @Test
    public void testsIfValueIsBetweenClosedBounds() {
        assertFalse(between(1, 10, 0));
        assertTrue(between(1, 10, 1));
        assertTrue(between(1, 10, 5));
        assertTrue(between(1, 10, 10));
        assertFalse(between(1, 10, 11));
    }
}
