package com.jnape.palatable.lambda.adt.coproduct;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.choice.Choice3;
import com.jnape.palatable.lambda.adt.choice.Choice4;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static org.junit.Assert.assertEquals;

public class CoProduct4Test {

    private CoProduct4<Integer, String, Boolean, Double, ?> a;
    private CoProduct4<Integer, String, Boolean, Double, ?> b;
    private CoProduct4<Integer, String, Boolean, Double, ?> c;
    private CoProduct4<Integer, String, Boolean, Double, ?> d;

    @Before
    public void setUp() throws Exception {
        a = new CoProduct4<Integer, String, Boolean, Double, CoProduct4<Integer, String, Boolean, Double, ?>>() {
            @Override
            public <R> R match(Function<? super Integer, ? extends R> aFn, Function<? super String, ? extends R> bFn,
                               Function<? super Boolean, ? extends R> cFn, Function<? super Double, ? extends R> dFn) {
                return aFn.apply(1);
            }
        };
        b = new CoProduct4<Integer, String, Boolean, Double, CoProduct4<Integer, String, Boolean, Double, ?>>() {
            @Override
            public <R> R match(Function<? super Integer, ? extends R> aFn, Function<? super String, ? extends R> bFn,
                               Function<? super Boolean, ? extends R> cFn, Function<? super Double, ? extends R> dFn) {
                return bFn.apply("two");
            }
        };
        c = new CoProduct4<Integer, String, Boolean, Double, CoProduct4<Integer, String, Boolean, Double, ?>>() {
            @Override
            public <R> R match(Function<? super Integer, ? extends R> aFn, Function<? super String, ? extends R> bFn,
                               Function<? super Boolean, ? extends R> cFn, Function<? super Double, ? extends R> dFn) {
                return cFn.apply(true);
            }
        };
        d = new CoProduct4<Integer, String, Boolean, Double, CoProduct4<Integer, String, Boolean, Double, ?>>() {
            @Override
            public <R> R match(Function<? super Integer, ? extends R> aFn, Function<? super String, ? extends R> bFn,
                               Function<? super Boolean, ? extends R> cFn, Function<? super Double, ? extends R> dFn) {
                return dFn.apply(4D);
            }
        };
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
        assertEquals(1, a.diverge().match(id(), id(), id(), id(), id()));
        assertEquals("two", b.diverge().match(id(), id(), id(), id(), id()));
        assertEquals(true, c.diverge().match(id(), id(), id(), id(), id()));
        assertEquals(4D, d.diverge().match(id(), id(), id(), id(), id()));
    }

    @Test
    public void converge() {
        Function<Double, CoProduct3<Integer, String, Boolean, ?>> convergenceFn = x -> x.equals(1d)
                                                                                       ? Choice3.a(1)
                                                                                       : x.equals(2d)
                                                                                         ? Choice3.b("b")
                                                                                         : Choice3.c(false);
        assertEquals(1, a.converge(convergenceFn).match(id(), id(), id()));
        assertEquals("two", b.converge(convergenceFn).match(id(), id(), id()));
        assertEquals(true, c.converge(convergenceFn).match(id(), id(), id()));
        assertEquals(1, Choice4.<Integer, String, Boolean, Double>d(1D).converge(convergenceFn).match(id(), id(), id()));
        assertEquals("b", Choice4.<Integer, String, Boolean, Double>d(2D).converge(convergenceFn).match(id(), id(), id()));
        assertEquals(false, Choice4.<Integer, String, Boolean, Double>d(3D).converge(convergenceFn).match(id(), id(), id()));
    }

    @Test
    public void projections() {
        assertEquals(tuple(just(1), nothing(), nothing(), nothing()), a.project());
        assertEquals(tuple(nothing(), just("two"), nothing(), nothing()), b.project());
        assertEquals(tuple(nothing(), nothing(), just(true), nothing()), c.project());
        assertEquals(tuple(nothing(), nothing(), nothing(), just(4D)), d.project());

        assertEquals(tuple(a.projectA(), a.projectB(), a.projectC(), a.projectD()), a.project());
        assertEquals(tuple(b.projectA(), b.projectB(), b.projectC(), b.projectD()), b.project());
        assertEquals(tuple(c.projectA(), c.projectB(), c.projectC(), c.projectD()), c.project());
        assertEquals(tuple(d.projectA(), d.projectB(), d.projectC(), d.projectD()), d.project());
    }

    @Test
    public void embed() {
        assertEquals(just(a), a.embed(Maybe::just, Maybe::just, Maybe::just, Maybe::just));
        assertEquals(just(b), b.embed(Maybe::just, Maybe::just, Maybe::just, Maybe::just));
        assertEquals(just(c), c.embed(Maybe::just, Maybe::just, Maybe::just, Maybe::just));
        assertEquals(just(d), d.embed(Maybe::just, Maybe::just, Maybe::just, Maybe::just));
    }
}