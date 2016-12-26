package com.jnape.palatable.lambda.adt.choice;

import org.junit.Before;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.choice.Choice3.b;
import static com.jnape.palatable.lambda.adt.choice.Choice3.c;
import static org.junit.Assert.assertEquals;

public class Choice3Test {

    private Choice3<Integer, String, Boolean> a;
    private Choice3<Integer, String, Boolean> b;
    private Choice3<Integer, String, Boolean> c;

    @Before
    public void setUp() {
        a = Choice3.a(1);
        b = Choice3.b("two");
        c = Choice3.c(true);
    }

    @Test
    public void convergeStaysInChoice() {
        assertEquals(Choice2.a(1), a.converge(c -> Choice2.b(c.toString())));
        assertEquals(Choice2.b("two"), b.converge(c -> Choice2.b(c.toString())));
        assertEquals(Choice2.b("true"), c.converge(c -> Choice2.b(c.toString())));
    }

    @Test
    public void divergeStaysInChoice() {
        assertEquals(Choice4.a(1), a.diverge());
        assertEquals(Choice4.b("two"), b.diverge());
        assertEquals(Choice4.c(true), c.diverge());
    }

    @Test
    public void functorProperties() {
        assertEquals(a, a.fmap(bool -> !bool));
        assertEquals(b, b.fmap(bool -> !bool));
        assertEquals(c(false), c.fmap(bool -> !bool));
    }

    @Test
    public void bifunctorProperties() {
        assertEquals(a, a.biMap(String::toUpperCase, bool -> !bool));
        assertEquals(b("TWO"), b.biMap(String::toUpperCase, bool -> !bool));
        assertEquals(c(false), c.biMap(String::toUpperCase, bool -> !bool));
    }
}