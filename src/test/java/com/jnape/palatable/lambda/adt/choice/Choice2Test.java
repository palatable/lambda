package com.jnape.palatable.lambda.adt.choice;

import org.junit.Before;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.choice.Choice2.a;
import static com.jnape.palatable.lambda.adt.choice.Choice2.b;
import static org.junit.Assert.assertEquals;

public class Choice2Test {

    private Choice2<Integer, Boolean> a;
    private Choice2<Integer, Boolean> b;

    @Before
    public void setUp() {
        a = a(1);
        b = b(true);
    }

    @Test
    public void divergeStaysInChoice() {
        assertEquals(Choice3.a(1), a.diverge());
        assertEquals(Choice3.b(true), b.diverge());
    }

    @Test
    public void functorProperties() {
        assertEquals(a, a.fmap(bool -> !bool));
        assertEquals(b(false), b.fmap(bool -> !bool));
    }

    @Test
    public void bifunctorProperties() {
        assertEquals(a(-1), a.biMap(i -> i * -1, bool -> !bool));
        assertEquals(b(false), b.biMap(i -> i * -1, bool -> !bool));
    }
}