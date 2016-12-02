package com.jnape.palatable.lambda.adt.coproduct;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static com.jnape.palatable.lambda.adt.coproduct.CoProduct3.a;
import static com.jnape.palatable.lambda.adt.coproduct.CoProduct3.b;
import static com.jnape.palatable.lambda.adt.coproduct.CoProduct3.c;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static org.junit.Assert.assertEquals;

public class CoProduct3Test {

    private CoProduct3<Integer, String, Boolean> a;
    private CoProduct3<Integer, String, Boolean> b;
    private CoProduct3<Integer, String, Boolean> c;

    @Before
    public void setUp() throws Exception {
        a = a(1);
        b = b("two");
        c = c(true);
    }

    @Test
    public void match() {
        assertEquals(1, a.match(id(), id(), id()));
        assertEquals("two", b.match(id(), id(), id()));
        assertEquals(true, c.match(id(), id(), id()));
    }

    @Test
    public void diverge() {
        assertEquals(CoProduct4.a(1), a.diverge());
        assertEquals(CoProduct4.b("two"), b.diverge());
        assertEquals(CoProduct4.c(true), c.diverge());
    }

    @Test
    public void project() {
        assertEquals(tuple(Optional.of(1), Optional.empty(), Optional.empty()), CoProduct3.a(1).project());
        assertEquals(tuple(Optional.empty(), Optional.of("b"), Optional.empty()), CoProduct3.b("b").project());
        assertEquals(tuple(Optional.empty(), Optional.empty(), Optional.of('c')), CoProduct3.c('c').project());
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