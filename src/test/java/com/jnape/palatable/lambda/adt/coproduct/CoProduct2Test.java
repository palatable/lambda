package com.jnape.palatable.lambda.adt.coproduct;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static com.jnape.palatable.lambda.adt.coproduct.CoProduct2.a;
import static com.jnape.palatable.lambda.adt.coproduct.CoProduct2.b;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static org.junit.Assert.assertEquals;

public class CoProduct2Test {

    private CoProduct2<Integer, Boolean> a;
    private CoProduct2<Integer, Boolean> b;

    @Before
    public void setUp() {
        a = a(1);
        b = b(true);
    }

    @Test
    public void match() {
        assertEquals(1, a.match(id(), id()));
        assertEquals(true, b.match(id(), id()));
    }

    @Test
    public void diverge() {
        assertEquals(CoProduct3.a(1), a.diverge());
        assertEquals(CoProduct3.b(true), b.diverge());
    }

    @Test
    public void project() {
        assertEquals(tuple(Optional.of(1), Optional.empty()), CoProduct2.a(1).project());
        assertEquals(tuple(Optional.empty(), Optional.of("b")), CoProduct2.b("b").project());
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