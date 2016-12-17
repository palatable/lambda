package com.jnape.palatable.lambda.adt.coproduct;

import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.CoProductProjections;

import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.coproduct.CoProduct3.a;
import static com.jnape.palatable.lambda.adt.coproduct.CoProduct3.b;
import static com.jnape.palatable.lambda.adt.coproduct.CoProduct3.c;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static org.junit.Assert.assertEquals;

@RunWith(Traits.class)
public class CoProduct3Test {

    private CoProduct3<Integer, String, Boolean> a;
    private CoProduct3<Integer, String, Boolean> b;
    private CoProduct3<Integer, String, Boolean> c;

    @Before
    public void setUp() {
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
    public void converge() {
        Function<Boolean, CoProduct2<Integer, String>> convergenceFn = x -> x ? CoProduct2.a(1) : CoProduct2.b("false");
        assertEquals(CoProduct2.a(1), a.converge(convergenceFn));
        assertEquals(CoProduct2.b("two"), b.converge(convergenceFn));
        assertEquals(CoProduct2.a(1), CoProduct3.<Integer, String, Boolean>c(true).converge(convergenceFn));
        assertEquals(CoProduct2.b("false"), CoProduct3.<Integer, String, Boolean>c(false).converge(convergenceFn));
    }

    @TestTraits({CoProductProjections.class})
    public Class<?> projections() {
        return CoProduct3.class;
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