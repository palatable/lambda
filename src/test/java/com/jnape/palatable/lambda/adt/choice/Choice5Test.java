package com.jnape.palatable.lambda.adt.choice;

import org.junit.Before;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.choice.Choice5.a;
import static com.jnape.palatable.lambda.adt.choice.Choice5.b;
import static com.jnape.palatable.lambda.adt.choice.Choice5.c;
import static com.jnape.palatable.lambda.adt.choice.Choice5.d;
import static com.jnape.palatable.lambda.adt.choice.Choice5.e;
import static org.junit.Assert.assertEquals;

public class Choice5Test {

    private Choice5<Integer, String, Boolean, Double, Character> a;
    private Choice5<Integer, String, Boolean, Double, Character> b;
    private Choice5<Integer, String, Boolean, Double, Character> c;
    private Choice5<Integer, String, Boolean, Double, Character> d;
    private Choice5<Integer, String, Boolean, Double, Character> e;

    @Before
    public void setUp() {
        a = a(1);
        b = b("two");
        c = c(true);
        d = d(4d);
        e = e('z');
    }

    @Test
    public void convergeStaysInChoice() {
        assertEquals(Choice4.a(1), a.converge(e -> Choice4.b(e.toString())));
        assertEquals(Choice4.b("two"), b.converge(e -> Choice4.b(e.toString())));
        assertEquals(Choice4.c(true), c.converge(e -> Choice4.b(e.toString())));
        assertEquals(Choice4.d(4d), d.converge(e -> Choice4.b(e.toString())));
        assertEquals(Choice4.b("z"), e.converge(e -> Choice4.b(e.toString())));
    }

    @Test
    public void functorProperties() {
        assertEquals(a, a.fmap(Character::toUpperCase));
        assertEquals(b, b.fmap(Character::toUpperCase));
        assertEquals(c, c.fmap(Character::toUpperCase));
        assertEquals(d, d.fmap(Character::toUpperCase));
        assertEquals(e('Z'), e.fmap(Character::toUpperCase));
    }

    @Test
    public void bifunctorProperties() {
        assertEquals(a, a.biMap(d -> -d, Character::toUpperCase));
        assertEquals(b, b.biMap(d -> -d, Character::toUpperCase));
        assertEquals(c, c.biMap(d -> -d, Character::toUpperCase));
        assertEquals(d(-4D), d.biMap(d -> -d, Character::toUpperCase));
        assertEquals(e('Z'), e.biMap(d -> -d, Character::toUpperCase));
    }
}