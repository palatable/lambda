package com.jnape.palatable.lambda.adt.coproduct;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.choice.Choice7;
import com.jnape.palatable.lambda.adt.choice.Choice8;
import com.jnape.palatable.lambda.functions.Fn1;
import org.junit.Before;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static org.junit.Assert.assertEquals;

public class CoProduct8Test {

    private CoProduct8<Integer, String, Boolean, Double, Character, Long, Float, Short, ?> a;
    private CoProduct8<Integer, String, Boolean, Double, Character, Long, Float, Short, ?> b;
    private CoProduct8<Integer, String, Boolean, Double, Character, Long, Float, Short, ?> c;
    private CoProduct8<Integer, String, Boolean, Double, Character, Long, Float, Short, ?> d;
    private CoProduct8<Integer, String, Boolean, Double, Character, Long, Float, Short, ?> e;
    private CoProduct8<Integer, String, Boolean, Double, Character, Long, Float, Short, ?> f;
    private CoProduct8<Integer, String, Boolean, Double, Character, Long, Float, Short, ?> g;
    private CoProduct8<Integer, String, Boolean, Double, Character, Long, Float, Short, ?> h;

    @Before
    public void setUp() {
        a = new CoProduct8<Integer, String, Boolean, Double, Character, Long, Float, Short, CoProduct8<Integer, String, Boolean, Double, Character, Long, Float, Short, ?>>() {
            @Override
            public <R> R match(Fn1<? super Integer, ? extends R> aFn, Fn1<? super String, ? extends R> bFn,
                               Fn1<? super Boolean, ? extends R> cFn, Fn1<? super Double, ? extends R> dFn,
                               Fn1<? super Character, ? extends R> eFn, Fn1<? super Long, ? extends R> fFn,
                               Fn1<? super Float, ? extends R> gFn, Fn1<? super Short, ? extends R> hFn) {
                return aFn.apply(1);
            }
        };
        b = new CoProduct8<Integer, String, Boolean, Double, Character, Long, Float, Short, CoProduct8<Integer, String, Boolean, Double, Character, Long, Float, Short, ?>>() {
            @Override
            public <R> R match(Fn1<? super Integer, ? extends R> aFn, Fn1<? super String, ? extends R> bFn,
                               Fn1<? super Boolean, ? extends R> cFn, Fn1<? super Double, ? extends R> dFn,
                               Fn1<? super Character, ? extends R> eFn, Fn1<? super Long, ? extends R> fFn,
                               Fn1<? super Float, ? extends R> gFn, Fn1<? super Short, ? extends R> hFn) {
                return bFn.apply("two");
            }
        };
        c = new CoProduct8<Integer, String, Boolean, Double, Character, Long, Float, Short, CoProduct8<Integer, String, Boolean, Double, Character, Long, Float, Short, ?>>() {
            @Override
            public <R> R match(Fn1<? super Integer, ? extends R> aFn, Fn1<? super String, ? extends R> bFn,
                               Fn1<? super Boolean, ? extends R> cFn, Fn1<? super Double, ? extends R> dFn,
                               Fn1<? super Character, ? extends R> eFn, Fn1<? super Long, ? extends R> fFn,
                               Fn1<? super Float, ? extends R> gFn, Fn1<? super Short, ? extends R> hFn) {
                return cFn.apply(true);
            }
        };
        d = new CoProduct8<Integer, String, Boolean, Double, Character, Long, Float, Short, CoProduct8<Integer, String, Boolean, Double, Character, Long, Float, Short, ?>>() {
            @Override
            public <R> R match(Fn1<? super Integer, ? extends R> aFn, Fn1<? super String, ? extends R> bFn,
                               Fn1<? super Boolean, ? extends R> cFn, Fn1<? super Double, ? extends R> dFn,
                               Fn1<? super Character, ? extends R> eFn, Fn1<? super Long, ? extends R> fFn,
                               Fn1<? super Float, ? extends R> gFn, Fn1<? super Short, ? extends R> hFn) {
                return dFn.apply(4D);
            }
        };
        e = new CoProduct8<Integer, String, Boolean, Double, Character, Long, Float, Short, CoProduct8<Integer, String, Boolean, Double, Character, Long, Float, Short, ?>>() {
            @Override
            public <R> R match(Fn1<? super Integer, ? extends R> aFn, Fn1<? super String, ? extends R> bFn,
                               Fn1<? super Boolean, ? extends R> cFn, Fn1<? super Double, ? extends R> dFn,
                               Fn1<? super Character, ? extends R> eFn, Fn1<? super Long, ? extends R> fFn,
                               Fn1<? super Float, ? extends R> gFn, Fn1<? super Short, ? extends R> hFn) {
                return eFn.apply('z');
            }
        };
        f = new CoProduct8<Integer, String, Boolean, Double, Character, Long, Float, Short, CoProduct8<Integer, String, Boolean, Double, Character, Long, Float, Short, ?>>() {
            @Override
            public <R> R match(Fn1<? super Integer, ? extends R> aFn, Fn1<? super String, ? extends R> bFn,
                               Fn1<? super Boolean, ? extends R> cFn, Fn1<? super Double, ? extends R> dFn,
                               Fn1<? super Character, ? extends R> eFn, Fn1<? super Long, ? extends R> fFn,
                               Fn1<? super Float, ? extends R> gFn, Fn1<? super Short, ? extends R> hFn) {
                return fFn.apply(5L);
            }
        };
        g = new CoProduct8<Integer, String, Boolean, Double, Character, Long, Float, Short, CoProduct8<Integer, String, Boolean, Double, Character, Long, Float, Short, ?>>() {
            @Override
            public <R> R match(Fn1<? super Integer, ? extends R> aFn, Fn1<? super String, ? extends R> bFn,
                               Fn1<? super Boolean, ? extends R> cFn, Fn1<? super Double, ? extends R> dFn,
                               Fn1<? super Character, ? extends R> eFn, Fn1<? super Long, ? extends R> fFn,
                               Fn1<? super Float, ? extends R> gFn, Fn1<? super Short, ? extends R> hFn) {
                return gFn.apply(6f);
            }
        };
        h = new CoProduct8<Integer, String, Boolean, Double, Character, Long, Float, Short, CoProduct8<Integer, String, Boolean, Double, Character, Long, Float, Short, ?>>() {
            @Override
            public <R> R match(Fn1<? super Integer, ? extends R> aFn, Fn1<? super String, ? extends R> bFn,
                               Fn1<? super Boolean, ? extends R> cFn, Fn1<? super Double, ? extends R> dFn,
                               Fn1<? super Character, ? extends R> eFn, Fn1<? super Long, ? extends R> fFn,
                               Fn1<? super Float, ? extends R> gFn, Fn1<? super Short, ? extends R> hFn) {
                return hFn.apply((short) 7);
            }
        };
    }

    @Test
    public void match() {
        assertEquals(1, a.match(id(), id(), id(), id(), id(), id(), id(), id()));
        assertEquals("two", b.match(id(), id(), id(), id(), id(), id(), id(), id()));
        assertEquals(true, c.match(id(), id(), id(), id(), id(), id(), id(), id()));
        assertEquals(4D, d.match(id(), id(), id(), id(), id(), id(), id(), id()));
        assertEquals('z', e.match(id(), id(), id(), id(), id(), id(), id(), id()));
        assertEquals(5L, f.match(id(), id(), id(), id(), id(), id(), id(), id()));
        assertEquals(6f, g.match(id(), id(), id(), id(), id(), id(), id(), id()));
        assertEquals((short) 7, h.match(id(), id(), id(), id(), id(), id(), id(), id()));
    }

    @Test
    public void converge() {
        Fn1<Short, CoProduct7<Integer, String, Boolean, Double, Character, Long, Float, ?>> convergenceFn = x ->
                x.equals((short) 1)
                ? Choice7.a(1)
                : x.equals((short) 2)
                  ? Choice7.b("b")
                  : x.equals((short) 3)
                    ? Choice7.c(false)
                    : x.equals((short) 4)
                      ? Choice7.d(1D)
                      : x.equals((short) 5)
                        ? Choice7.e('a')
                        : x.equals((short) 6)
                          ? Choice7.f(5L)
                          : Choice7.g(6F);
        assertEquals(1, a.converge(convergenceFn).match(id(), id(), id(), id(), id(), id(), id()));
        assertEquals("two", b.converge(convergenceFn).match(id(), id(), id(), id(), id(), id(), id()));
        assertEquals(true, c.converge(convergenceFn).match(id(), id(), id(), id(), id(), id(), id()));
        assertEquals(4D, d.converge(convergenceFn).match(id(), id(), id(), id(), id(), id(), id()));
        assertEquals('z', e.converge(convergenceFn).match(id(), id(), id(), id(), id(), id(), id()));
        assertEquals(5L, f.converge(convergenceFn).match(id(), id(), id(), id(), id(), id(), id()));
        assertEquals(6F, g.converge(convergenceFn).match(id(), id(), id(), id(), id(), id(), id()));
        assertEquals(1, Choice8.<Integer, String, Boolean, Double, Character, Long, Float, Short>h((short) 1).converge(convergenceFn).match(id(), id(), id(), id(), id(), id(), id()));
        assertEquals("b", Choice8.<Integer, String, Boolean, Double, Character, Long, Float, Short>h((short) 2).converge(convergenceFn).match(id(), id(), id(), id(), id(), id(), id()));
        assertEquals(false, Choice8.<Integer, String, Boolean, Double, Character, Long, Float, Short>h((short) 3).converge(convergenceFn).match(id(), id(), id(), id(), id(), id(), id()));
        assertEquals(1d, Choice8.<Integer, String, Boolean, Double, Character, Long, Float, Short>h((short) 4).converge(convergenceFn).match(id(), id(), id(), id(), id(), id(), id()));
        assertEquals('a', Choice8.<Integer, String, Boolean, Double, Character, Long, Float, Short>h((short) 5).converge(convergenceFn).match(id(), id(), id(), id(), id(), id(), id()));
        assertEquals(5L, Choice8.<Integer, String, Boolean, Double, Character, Long, Float, Short>h((short) 6).converge(convergenceFn).match(id(), id(), id(), id(), id(), id(), id()));
        assertEquals(6F, Choice8.<Integer, String, Boolean, Double, Character, Long, Float, Short>h((short) 7).converge(convergenceFn).match(id(), id(), id(), id(), id(), id(), id()));
    }

    @Test
    public void projections() {
        assertEquals(tuple(just(1), nothing(), nothing(), nothing(), nothing(), nothing(), nothing(), nothing()), a.project());
        assertEquals(tuple(nothing(), just("two"), nothing(), nothing(), nothing(), nothing(), nothing(), nothing()), b.project());
        assertEquals(tuple(nothing(), nothing(), just(true), nothing(), nothing(), nothing(), nothing(), nothing()), c.project());
        assertEquals(tuple(nothing(), nothing(), nothing(), just(4D), nothing(), nothing(), nothing(), nothing()), d.project());
        assertEquals(tuple(nothing(), nothing(), nothing(), nothing(), just('z'), nothing(), nothing(), nothing()), e.project());
        assertEquals(tuple(nothing(), nothing(), nothing(), nothing(), nothing(), just(5L), nothing(), nothing()), f.project());
        assertEquals(tuple(nothing(), nothing(), nothing(), nothing(), nothing(), nothing(), just(6F), nothing()), g.project());
        assertEquals(tuple(nothing(), nothing(), nothing(), nothing(), nothing(), nothing(), nothing(), just((short) 7)), h.project());

        assertEquals(tuple(a.projectA(), a.projectB(), a.projectC(), a.projectD(), a.projectE(), a.projectF(), a.projectG(), a.projectH()), a.project());
        assertEquals(tuple(b.projectA(), b.projectB(), b.projectC(), b.projectD(), b.projectE(), b.projectF(), b.projectG(), b.projectH()), b.project());
        assertEquals(tuple(c.projectA(), c.projectB(), c.projectC(), c.projectD(), c.projectE(), c.projectF(), c.projectG(), c.projectH()), c.project());
        assertEquals(tuple(d.projectA(), d.projectB(), d.projectC(), d.projectD(), d.projectE(), d.projectF(), d.projectG(), d.projectH()), d.project());
        assertEquals(tuple(e.projectA(), e.projectB(), e.projectC(), e.projectD(), e.projectE(), e.projectF(), e.projectG(), e.projectH()), e.project());
        assertEquals(tuple(f.projectA(), f.projectB(), f.projectC(), f.projectD(), f.projectE(), f.projectF(), f.projectG(), f.projectH()), f.project());
        assertEquals(tuple(g.projectA(), g.projectB(), g.projectC(), g.projectD(), g.projectE(), g.projectF(), g.projectG(), g.projectH()), g.project());
        assertEquals(tuple(h.projectA(), h.projectB(), h.projectC(), h.projectD(), h.projectE(), h.projectF(), h.projectG(), h.projectH()), h.project());
    }

    @Test
    public void embed() {
        assertEquals(just(a), a.embed(Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just));
        assertEquals(just(b), b.embed(Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just));
        assertEquals(just(c), c.embed(Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just));
        assertEquals(just(d), d.embed(Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just));
        assertEquals(just(e), e.embed(Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just));
        assertEquals(just(f), f.embed(Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just));
        assertEquals(just(g), g.embed(Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just));
        assertEquals(just(h), h.embed(Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just));
    }
}