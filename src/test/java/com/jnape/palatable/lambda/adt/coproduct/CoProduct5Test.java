package com.jnape.palatable.lambda.adt.coproduct;

import org.junit.Before;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.coproduct.CoProduct5.a;
import static com.jnape.palatable.lambda.adt.coproduct.CoProduct5.b;
import static com.jnape.palatable.lambda.adt.coproduct.CoProduct5.c;
import static com.jnape.palatable.lambda.adt.coproduct.CoProduct5.d;
import static com.jnape.palatable.lambda.adt.coproduct.CoProduct5.e;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static org.junit.Assert.assertEquals;

public class CoProduct5Test {

    private CoProduct5<Integer, String, Boolean, Double, Character> a;
    private CoProduct5<Integer, String, Boolean, Double, Character> b;
    private CoProduct5<Integer, String, Boolean, Double, Character> c;
    private CoProduct5<Integer, String, Boolean, Double, Character> d;
    private CoProduct5<Integer, String, Boolean, Double, Character> e;

    @Before
    public void setUp() {
        a = a(1);
        b = b("two");
        c = c(true);
        d = d(4D);
        e = e('z');
    }

    @Test
    public void match() {
        assertEquals(1, a.match(id(), id(), id(), id(), id()));
        assertEquals("two", b.match(id(), id(), id(), id(), id()));
        assertEquals(true, c.match(id(), id(), id(), id(), id()));
        assertEquals(4D, d.match(id(), id(), id(), id(), id()));
        assertEquals('z', e.match(id(), id(), id(), id(), id()));
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