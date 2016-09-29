package com.jnape.palatable.lambda.semigroup;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static org.junit.Assert.assertEquals;

public class RightTest {
    private static final Semigroup<Integer> ADDITION = (x, y) -> x + y;

    @Test
    public void appliesTwoRightValues() {
        assertEquals(right(3), new Right<>(ADDITION).apply(right(1), right(2)));
    }

    @Test
    public void leftValueSubvertsSemigroup() {
        assertEquals(left("foo"), new Right<>(ADDITION).apply(left("foo"), right(1)));
        assertEquals(left("bar"), new Right<>(ADDITION).apply(right(1), left("bar")));
        assertEquals(left("foo"), new Right<>(ADDITION).apply(left("foo"), left("bar")));
    }
}