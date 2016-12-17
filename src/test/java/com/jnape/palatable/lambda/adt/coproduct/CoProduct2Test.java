package com.jnape.palatable.lambda.adt.coproduct;

import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.CoProductProjections;

import static com.jnape.palatable.lambda.adt.coproduct.CoProduct2.a;
import static com.jnape.palatable.lambda.adt.coproduct.CoProduct2.b;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static org.junit.Assert.assertEquals;

@RunWith(Traits.class)
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

    @TestTraits({CoProductProjections.class})
    public Class<?> projections() {
        return CoProduct2.class;
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