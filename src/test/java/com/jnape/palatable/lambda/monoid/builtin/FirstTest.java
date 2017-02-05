package com.jnape.palatable.lambda.monoid.builtin;

import org.junit.Test;

import java.util.Optional;

import static com.jnape.palatable.lambda.monoid.builtin.First.first;
import static org.junit.Assert.assertEquals;

public class FirstTest {

    @Test
    public void identity() {
        assertEquals(Optional.empty(), first().identity());
    }

    @Test
    public void monoid() {
        First<Integer> first = first();
        assertEquals(Optional.of(1), first.apply(Optional.of(1), Optional.of(2)));
        assertEquals(Optional.of(1), first.apply(Optional.of(1), Optional.empty()));
        assertEquals(Optional.of(2), first.apply(Optional.empty(), Optional.of(2)));
        assertEquals(Optional.empty(), first.apply(Optional.empty(), Optional.empty()));
    }
}