package com.jnape.palatable.lambda.semigroup;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static org.junit.Assert.assertEquals;

public class LeftTest {
    private static final Semigroup<Integer> ADDITION = (x, y) -> x + y;

    @Test
    public void appliesTwoLeftValues() {
        assertEquals(left(3), new Left<>(ADDITION).apply(left(1), left(2)));
    }

    @Test
    public void rightValueSubvertsSemigroup() {
        assertEquals(right("foo"), new Left<>(ADDITION).apply(right("foo"), left(1)));
        assertEquals(right("bar"), new Left<>(ADDITION).apply(left(1), right("bar")));
        assertEquals(right("foo"), new Left<>(ADDITION).apply(right("foo"), right("bar")));
    }
}