package com.jnape.palatable.lambda.functions.builtin.fn2;

import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Eq.eq;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EqTest {

    @Test
    public void testsValueEquality() {
        Eq<String> stringEq = eq();

        assertTrue(stringEq.apply("a", "a"));
        assertFalse(stringEq.apply("a", "b"));
        assertFalse(stringEq.apply("b", "a"));
        assertTrue(stringEq.apply(null, null));
        assertFalse(stringEq.apply("a", null));
        assertFalse(stringEq.apply(null, "a"));
    }
}