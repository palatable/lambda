package com.jnape.palatable.lambda.monoid.builtin;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static com.jnape.palatable.lambda.monoid.builtin.Trivial.trivial;
import static org.junit.Assert.assertEquals;

public class TrivialTest {

    @Test
    public void triviality() {
        assertEquals(UNIT, trivial().identity());
        assertEquals(UNIT, trivial().apply(UNIT, UNIT));
    }
}