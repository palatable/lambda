package com.jnape.palatable.lambda.adt.coproduct;

import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.CoProductProjections;

import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.coproduct.CoProduct4.a;
import static com.jnape.palatable.lambda.adt.coproduct.CoProduct4.b;
import static com.jnape.palatable.lambda.adt.coproduct.CoProduct4.c;
import static com.jnape.palatable.lambda.adt.coproduct.CoProduct4.d;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static org.junit.Assert.assertEquals;

@RunWith(Traits.class)
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
    public void converge() {
        Function<Double, CoProduct3<Integer, String, Boolean>> convergenceFn = x -> x.equals(1d)
                ? CoProduct3.a(1)
                : x.equals(2d)
                ? CoProduct3.b("b")
                : CoProduct3.c(false);
        assertEquals(CoProduct3.a(1), a.converge(convergenceFn));
        assertEquals(CoProduct3.b("two"), b.converge(convergenceFn));
        assertEquals(CoProduct3.c(true), c.converge(convergenceFn));
        assertEquals(CoProduct3.a(1), CoProduct4.<Integer, String, Boolean, Double>d(1d).converge(convergenceFn));
        assertEquals(CoProduct3.b("b"), CoProduct4.<Integer, String, Boolean, Double>d(2d).converge(convergenceFn));
        assertEquals(CoProduct3.c(false), CoProduct4.<Integer, String, Boolean, Double>d(3d).converge(convergenceFn));
    }

    @TestTraits({CoProductProjections.class})
    public Class<?> projections() {
        return CoProduct4.class;
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