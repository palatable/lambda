package com.jnape.palatable.lambda.monoid.builtin;

import org.junit.Test;

import static com.jnape.palatable.lambda.monoid.builtin.And.and;
import static org.junit.Assert.assertEquals;

public class AndTest {

    @Test
    public void identity() {
        assertEquals(true, and().identity());
    }

    @Test
    public void monoid() {
        And and = and();
        assertEquals(true, and.apply(true, true));
        assertEquals(false, and.apply(false, true));
        assertEquals(false, and.apply(true, false));
        assertEquals(false, and.apply(false, false));
    }
}