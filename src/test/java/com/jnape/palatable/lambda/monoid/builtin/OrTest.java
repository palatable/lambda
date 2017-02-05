package com.jnape.palatable.lambda.monoid.builtin;

import org.junit.Test;

import static com.jnape.palatable.lambda.monoid.builtin.Or.or;
import static org.junit.Assert.assertEquals;

public class OrTest {

    @Test
    public void identity() {
        assertEquals(false, or().identity());
    }

    @Test
    public void monoid() {
        Or or = or();
        assertEquals(true, or.apply(true, true));
        assertEquals(true, or.apply(true, false));
        assertEquals(true, or.apply(false, true));
        assertEquals(false, or.apply(false, false));
    }
}