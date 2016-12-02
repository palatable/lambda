package com.jnape.palatable.lambda.adt.coproduct;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static com.jnape.palatable.lambda.adt.coproduct.CoProduct4.a;
import static com.jnape.palatable.lambda.adt.coproduct.CoProduct4.b;
import static com.jnape.palatable.lambda.adt.coproduct.CoProduct4.c;
import static com.jnape.palatable.lambda.adt.coproduct.CoProduct4.d;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static org.junit.Assert.assertEquals;

public class CoProduct4Test {

    private CoProduct4<Integer, String, Boolean, Double> a;
    private CoProduct4<Integer, String, Boolean, Double> b;
    private CoProduct4<Integer, String, Boolean, Double> c;
    private CoProduct4<Integer, String, Boolean, Double> d;

    @Before
    public void setUp() throws Exception {
        a = a(1);
        b = b("two");
        c = c(true);
        d = d(4D);
    }

    @Test
    public void match() {
        assertEquals(1, a.match(id(), id(), id(), id()));
        assertEquals("two", b.match(id(), id(), id(), id()));
        assertEquals(true, c.match(id(), id(), id(), id()));
        assertEquals(4D, d.match(id(), id(), id(), id()));
    }

    @Test
    public void diverge() {
        assertEquals(CoProduct5.a(1), a.diverge());
        assertEquals(CoProduct5.b("two"), b.diverge());
        assertEquals(CoProduct5.c(true), c.diverge());
        assertEquals(CoProduct5.d(4D), d.diverge());
    }

    @Test
    public void project() {
        assertEquals(tuple(Optional.of(1), Optional.empty(), Optional.empty(), Optional.empty()), CoProduct4.a(1).project());
        assertEquals(tuple(Optional.empty(), Optional.of("b"), Optional.empty(), Optional.empty()), CoProduct4.b("b").project());
        assertEquals(tuple(Optional.empty(), Optional.empty(), Optional.of('c'), Optional.empty()), CoProduct4.c('c').project());
        assertEquals(tuple(Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(4L)), CoProduct4.d(4L).project());
    }

    @Test
    public void functorProperties() {
        assertEquals(a, a.fmap(d -> -d));
        assertEquals(b, b.fmap(d -> -d));
        assertEquals(c, c.fmap(d -> -d));
        assertEquals(d(-4D), d.fmap(d -> -d));
    }

    @Test
    public void bifunctorProperties() {
        assertEquals(a, a.biMap(c -> !c, d -> -d));
        assertEquals(b, b.biMap(c -> !c, d -> -d));
        assertEquals(c(false), c.biMap(c -> !c, d -> -d));
        assertEquals(d(-4D), d.biMap(c -> !c, d -> -d));
    }
}