package com.jnape.palatable.lambda.monoid.builtin;

import org.junit.Test;

import static com.jnape.palatable.lambda.monoid.builtin.Xor.xor;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class XorTest {

    @Test
    public void identity() {
        assertFalse(xor().identity());
    }

    @Test
    public void monoid() {
        assertFalse(xor(false, false));
        assertTrue(xor(true, false));
        assertTrue(xor(false, true));
        assertFalse(xor(true, true));

        assertTrue(xor().reduceLeft(asList(true, false, false, true, true)));
        assertFalse(xor().reduceLeft(asList(true, false, true, false, false)));
    }
}