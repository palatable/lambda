package com.jnape.palatable.lambda.adt.coproduct;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.choice.Choice5;
import com.jnape.palatable.lambda.adt.choice.Choice6;
import com.jnape.palatable.lambda.functions.Fn1;
import org.junit.Before;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static org.junit.Assert.assertEquals;

public class CoProduct6Test {

    private CoProduct6<Integer, String, Boolean, Double, Character, Long, ?> a;
    private CoProduct6<Integer, String, Boolean, Double, Character, Long, ?> b;
    private CoProduct6<Integer, String, Boolean, Double, Character, Long, ?> c;
    private CoProduct6<Integer, String, Boolean, Double, Character, Long, ?> d;
    private CoProduct6<Integer, String, Boolean, Double, Character, Long, ?> e;
    private CoProduct6<Integer, String, Boolean, Double, Character, Long, ?> f;

    @Before
    public void setUp() {
        a = new CoProduct6<Integer, String, Boolean, Double, Character, Long, CoProduct6<Integer, String, Boolean, Double, Character, Long, ?>>() {
            @Override
            public <R> R match(Fn1<? super Integer, ? extends R> aFn, Fn1<? super String, ? extends R> bFn,
                               Fn1<? super Boolean, ? extends R> cFn, Fn1<? super Double, ? extends R> dFn,
                               Fn1<? super Character, ? extends R> eFn, Fn1<? super Long, ? extends R> fFn) {
                return aFn.apply(1);
            }
        };
        b = new CoProduct6<Integer, String, Boolean, Double, Character, Long, CoProduct6<Integer, String, Boolean, Double, Character, Long, ?>>() {
            @Override
            public <R> R match(Fn1<? super Integer, ? extends R> aFn, Fn1<? super String, ? extends R> bFn,
                               Fn1<? super Boolean, ? extends R> cFn, Fn1<? super Double, ? extends R> dFn,
                               Fn1<? super Character, ? extends R> eFn, Fn1<? super Long, ? extends R> fFn) {
                return bFn.apply("two");
            }
        };
        c = new CoProduct6<Integer, String, Boolean, Double, Character, Long, CoProduct6<Integer, String, Boolean, Double, Character, Long, ?>>() {
            @Override
            public <R> R match(Fn1<? super Integer, ? extends R> aFn, Fn1<? super String, ? extends R> bFn,
                               Fn1<? super Boolean, ? extends R> cFn, Fn1<? super Double, ? extends R> dFn,
                               Fn1<? super Character, ? extends R> eFn, Fn1<? super Long, ? extends R> fFn) {
                return cFn.apply(true);
            }
        };
        d = new CoProduct6<Integer, String, Boolean, Double, Character, Long, CoProduct6<Integer, String, Boolean, Double, Character, Long, ?>>() {
            @Override
            public <R> R match(Fn1<? super Integer, ? extends R> aFn, Fn1<? super String, ? extends R> bFn,
                               Fn1<? super Boolean, ? extends R> cFn, Fn1<? super Double, ? extends R> dFn,
                               Fn1<? super Character, ? extends R> eFn, Fn1<? super Long, ? extends R> fFn) {
                return dFn.apply(4D);
            }
        };
        e = new CoProduct6<Integer, String, Boolean, Double, Character, Long, CoProduct6<Integer, String, Boolean, Double, Character, Long, ?>>() {
            @Override
            public <R> R match(Fn1<? super Integer, ? extends R> aFn, Fn1<? super String, ? extends R> bFn,
                               Fn1<? super Boolean, ? extends R> cFn, Fn1<? super Double, ? extends R> dFn,
                               Fn1<? super Character, ? extends R> eFn, Fn1<? super Long, ? extends R> fFn) {
                return eFn.apply('z');
            }
        };
        f = new CoProduct6<Integer, String, Boolean, Double, Character, Long, CoProduct6<Integer, String, Boolean, Double, Character, Long, ?>>() {
            @Override
            public <R> R match(Fn1<? super Integer, ? extends R> aFn, Fn1<? super String, ? extends R> bFn,
                               Fn1<? super Boolean, ? extends R> cFn, Fn1<? super Double, ? extends R> dFn,
                               Fn1<? super Character, ? extends R> eFn, Fn1<? super Long, ? extends R> fFn) {
                return fFn.apply(5L);
            }
        };
    }

    @Test
    public void match() {
        assertEquals(1, a.match(id(), id(), id(), id(), id(), id()));
        assertEquals("two", b.match(id(), id(), id(), id(), id(), id()));
        assertEquals(true, c.match(id(), id(), id(), id(), id(), id()));
        assertEquals(4D, d.match(id(), id(), id(), id(), id(), id()));
        assertEquals('z', e.match(id(), id(), id(), id(), id(), id()));
        assertEquals(5L, f.match(id(), id(), id(), id(), id(), id()));
    }

    @Test
    public void diverge() {
        assertEquals(1, a.diverge().match(id(), id(), id(), id(), id(), id(), id()));
        assertEquals("two", b.diverge().match(id(), id(), id(), id(), id(), id(), id()));
        assertEquals(true, c.diverge().match(id(), id(), id(), id(), id(), id(), id()));
        assertEquals(4D, d.diverge().match(id(), id(), id(), id(), id(), id(), id()));
        assertEquals('z', e.diverge().match(id(), id(), id(), id(), id(), id(), id()));
        assertEquals(5L, f.diverge().match(id(), id(), id(), id(), id(), id(), id()));
    }

    @Test
    public void converge() {
        Fn1<Long, CoProduct5<Integer, String, Boolean, Double, Character, ?>> convergenceFn = x ->
                x.equals(1L)
                ? Choice5.a(1)
                : x.equals(2L)
                  ? Choice5.b("b")
                  : x.equals(3L)
                    ? Choice5.c(false)
                    : x.equals(4L)
                      ? Choice5.d(1D)
                      : Choice5.e('a');
        assertEquals(1, a.converge(convergenceFn).match(id(), id(), id(), id(), id()));
        assertEquals("two", b.converge(convergenceFn).match(id(), id(), id(), id(), id()));
        assertEquals(true, c.converge(convergenceFn).match(id(), id(), id(), id(), id()));
        assertEquals(4D, d.converge(convergenceFn).match(id(), id(), id(), id(), id()));
        assertEquals('z', e.converge(convergenceFn).match(id(), id(), id(), id(), id()));
        assertEquals(1, Choice6.<Integer, String, Boolean, Double, Character, Long>f(1L).converge(convergenceFn).match(id(), id(), id(), id(), id()));
        assertEquals("b", Choice6.<Integer, String, Boolean, Double, Character, Long>f(2L).converge(convergenceFn).match(id(), id(), id(), id(), id()));
        assertEquals(false, Choice6.<Integer, String, Boolean, Double, Character, Long>f(3L).converge(convergenceFn).match(id(), id(), id(), id(), id()));
        assertEquals(1d, Choice6.<Integer, String, Boolean, Double, Character, Long>f(4L).converge(convergenceFn).match(id(), id(), id(), id(), id()));
        assertEquals('a', Choice6.<Integer, String, Boolean, Double, Character, Long>f(5L).converge(convergenceFn).match(id(), id(), id(), id(), id()));
    }

    @Test
    public void projections() {
        assertEquals(tuple(just(1), nothing(), nothing(), nothing(), nothing(), nothing()), a.project());
        assertEquals(tuple(nothing(), just("two"), nothing(), nothing(), nothing(), nothing()), b.project());
        assertEquals(tuple(nothing(), nothing(), just(true), nothing(), nothing(), nothing()), c.project());
        assertEquals(tuple(nothing(), nothing(), nothing(), just(4D), nothing(), nothing()), d.project());
        assertEquals(tuple(nothing(), nothing(), nothing(), nothing(), just('z'), nothing()), e.project());
        assertEquals(tuple(nothing(), nothing(), nothing(), nothing(), nothing(), just(5L)), f.project());

        assertEquals(tuple(a.projectA(), a.projectB(), a.projectC(), a.projectD(), a.projectE(), a.projectF()), a.project());
        assertEquals(tuple(b.projectA(), b.projectB(), b.projectC(), b.projectD(), b.projectE(), b.projectF()), b.project());
        assertEquals(tuple(c.projectA(), c.projectB(), c.projectC(), c.projectD(), c.projectE(), c.projectF()), c.project());
        assertEquals(tuple(d.projectA(), d.projectB(), d.projectC(), d.projectD(), d.projectE(), d.projectF()), d.project());
        assertEquals(tuple(e.projectA(), e.projectB(), e.projectC(), e.projectD(), e.projectE(), e.projectF()), e.project());
        assertEquals(tuple(f.projectA(), f.projectB(), f.projectC(), f.projectD(), f.projectE(), f.projectF()), f.project());
    }

    @Test
    public void embed() {
        assertEquals(just(a), a.embed(Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just));
        assertEquals(just(b), b.embed(Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just));
        assertEquals(just(c), c.embed(Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just));
        assertEquals(just(d), d.embed(Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just));
        assertEquals(just(e), e.embed(Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just));
        assertEquals(just(f), f.embed(Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just));
    }
}