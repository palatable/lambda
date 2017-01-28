package com.jnape.palatable.lambda.monoid.builtin;

import org.junit.Test;

import static com.jnape.palatable.lambda.monoid.builtin.Join.join;
import static org.junit.Assert.assertEquals;

public class JoinTest {

    @Test
    public void identity() {
        assertEquals("", join().identity());
    }

    @Test
    public void monoid() {
        assertEquals("ab", join().apply("a", "b"));
        assertEquals("a", join().apply("a", ""));
        assertEquals("b", join().apply("", "b"));
        assertEquals("", join().apply("", ""));
    }
}