package com.jnape.palatable.lambda.monoid.builtin;

import org.junit.Test;

import java.util.Optional;

import static com.jnape.palatable.lambda.monoid.builtin.Last.last;
import static org.junit.Assert.assertEquals;

public class LastTest {

    @Test
    public void identity() {
        assertEquals(Optional.empty(), last().identity());
    }

    @Test
    public void monoid() {
        Last<Integer> last = last();
        assertEquals(Optional.of(2), last.apply(Optional.of(1), Optional.of(2)));
        assertEquals(Optional.of(2), last.apply(Optional.empty(), Optional.of(2)));
        assertEquals(Optional.of(1), last.apply(Optional.of(1), Optional.empty()));
        assertEquals(Optional.empty(), last.apply(Optional.empty(), Optional.empty()));
    }
}