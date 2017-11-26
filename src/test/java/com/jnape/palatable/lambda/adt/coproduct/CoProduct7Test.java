package com.jnape.palatable.lambda.adt.coproduct;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.choice.Choice6;
import com.jnape.palatable.lambda.adt.choice.Choice7;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static org.junit.Assert.assertEquals;

public class CoProduct7Test {

    private CoProduct7<Integer, String, Boolean, Double, Character, Long, Float, ?> a;
    private CoProduct7<Integer, String, Boolean, Double, Character, Long, Float, ?> b;
    private CoProduct7<Integer, String, Boolean, Double, Character, Long, Float, ?> c;
    private CoProduct7<Integer, String, Boolean, Double, Character, Long, Float, ?> d;
    private CoProduct7<Integer, String, Boolean, Double, Character, Long, Float, ?> e;
    private CoProduct7<Integer, String, Boolean, Double, Character, Long, Float, ?> f;
    private CoProduct7<Integer, String, Boolean, Double, Character, Long, Float, ?> g;

    @Before
    public void setUp() {
        a = new CoProduct7<Integer, String, Boolean, Double, Character, Long, Float, CoProduct7<Integer, String, Boolean, Double, Character, Long, Float, ?>>() {
            @Override
            public <R> R match(Function<? super Integer, ? extends R> aFn, Function<? super String, ? extends R> bFn,
                               Function<? super Boolean, ? extends R> cFn, Function<? super Double, ? extends R> dFn,
                               Function<? super Character, ? extends R> eFn, Function<? super Long, ? extends R> fFn,
                               Function<? super Float, ? extends R> gFn) {
                return aFn.apply(1);
            }
        };
        b = new CoProduct7<Integer, String, Boolean, Double, Character, Long, Float, CoProduct7<Integer, String, Boolean, Double, Character, Long, Float, ?>>() {
            @Override
            public <R> R match(Function<? super Integer, ? extends R> aFn, Function<? super String, ? extends R> bFn,
                               Function<? super Boolean, ? extends R> cFn, Function<? super Double, ? extends R> dFn,
                               Function<? super Character, ? extends R> eFn, Function<? super Long, ? extends R> fFn,
                               Function<? super Float, ? extends R> gFn) {
                return bFn.apply("two");
            }
        };
        c = new CoProduct7<Integer, String, Boolean, Double, Character, Long, Float, CoProduct7<Integer, String, Boolean, Double, Character, Long, Float, ?>>() {
            @Override
            public <R> R match(Function<? super Integer, ? extends R> aFn, Function<? super String, ? extends R> bFn,
                               Function<? super Boolean, ? extends R> cFn, Function<? super Double, ? extends R> dFn,
                               Function<? super Character, ? extends R> eFn, Function<? super Long, ? extends R> fFn,
                               Function<? super Float, ? extends R> gFn) {
                return cFn.apply(true);
            }
        };
        d = new CoProduct7<Integer, String, Boolean, Double, Character, Long, Float, CoProduct7<Integer, String, Boolean, Double, Character, Long, Float, ?>>() {
            @Override
            public <R> R match(Function<? super Integer, ? extends R> aFn, Function<? super String, ? extends R> bFn,
                               Function<? super Boolean, ? extends R> cFn, Function<? super Double, ? extends R> dFn,
                               Function<? super Character, ? extends R> eFn, Function<? super Long, ? extends R> fFn,
                               Function<? super Float, ? extends R> gFn) {
                return dFn.apply(4D);
            }
        };
        e = new CoProduct7<Integer, String, Boolean, Double, Character, Long, Float, CoProduct7<Integer, String, Boolean, Double, Character, Long, Float, ?>>() {
            @Override
            public <R> R match(Function<? super Integer, ? extends R> aFn, Function<? super String, ? extends R> bFn,
                               Function<? super Boolean, ? extends R> cFn, Function<? super Double, ? extends R> dFn,
                               Function<? super Character, ? extends R> eFn, Function<? super Long, ? extends R> fFn,
                               Function<? super Float, ? extends R> gFn) {
                return eFn.apply('z');
            }
        };
        f = new CoProduct7<Integer, String, Boolean, Double, Character, Long, Float, CoProduct7<Integer, String, Boolean, Double, Character, Long, Float, ?>>() {
            @Override
            public <R> R match(Function<? super Integer, ? extends R> aFn, Function<? super String, ? extends R> bFn,
                               Function<? super Boolean, ? extends R> cFn, Function<? super Double, ? extends R> dFn,
                               Function<? super Character, ? extends R> eFn, Function<? super Long, ? extends R> fFn,
                               Function<? super Float, ? extends R> gFn) {
                return fFn.apply(5L);
            }
        };
        g = new CoProduct7<Integer, String, Boolean, Double, Character, Long, Float, CoProduct7<Integer, String, Boolean, Double, Character, Long, Float, ?>>() {
            @Override
            public <R> R match(Function<? super Integer, ? extends R> aFn, Function<? super String, ? extends R> bFn,
                               Function<? super Boolean, ? extends R> cFn, Function<? super Double, ? extends R> dFn,
                               Function<? super Character, ? extends R> eFn, Function<? super Long, ? extends R> fFn,
                               Function<? super Float, ? extends R> gFn) {
                return gFn.apply(6f);
            }
        };
    }

    @Test
    public void match() {
        assertEquals(1, a.match(id(), id(), id(), id(), id(), id(), id()));
        assertEquals("two", b.match(id(), id(), id(), id(), id(), id(), id()));
        assertEquals(true, c.match(id(), id(), id(), id(), id(), id(), id()));
        assertEquals(4D, d.match(id(), id(), id(), id(), id(), id(), id()));
        assertEquals('z', e.match(id(), id(), id(), id(), id(), id(), id()));
        assertEquals(5L, f.match(id(), id(), id(), id(), id(), id(), id()));
        assertEquals(6f, g.match(id(), id(), id(), id(), id(), id(), id()));
    }

    @Test
    public void diverge() {
        assertEquals(1, a.diverge().match(id(), id(), id(), id(), id(), id(), id(), id()));
        assertEquals("two", b.diverge().match(id(), id(), id(), id(), id(), id(), id(), id()));
        assertEquals(true, c.diverge().match(id(), id(), id(), id(), id(), id(), id(), id()));
        assertEquals(4D, d.diverge().match(id(), id(), id(), id(), id(), id(), id(), id()));
        assertEquals('z', e.diverge().match(id(), id(), id(), id(), id(), id(), id(), id()));
        assertEquals(5L, f.diverge().match(id(), id(), id(), id(), id(), id(), id(), id()));
        assertEquals(6F, g.diverge().match(id(), id(), id(), id(), id(), id(), id(), id()));
    }

    @Test
    public void converge() {
        Function<Float, CoProduct6<Integer, String, Boolean, Double, Character, Long, ?>> convergenceFn = x ->
                x.equals(1f)
                ? Choice6.a(1)
                : x.equals(2f)
                  ? Choice6.b("b")
                  : x.equals(3f)
                    ? Choice6.c(false)
                    : x.equals(4f)
                      ? Choice6.d(1D)
                      : x.equals(5f)
                        ? Choice6.e('a')
                        : Choice6.f(5L);
        assertEquals(1, a.converge(convergenceFn).match(id(), id(), id(), id(), id(), id()));
        assertEquals("two", b.converge(convergenceFn).match(id(), id(), id(), id(), id(), id()));
        assertEquals(true, c.converge(convergenceFn).match(id(), id(), id(), id(), id(), id()));
        assertEquals(4D, d.converge(convergenceFn).match(id(), id(), id(), id(), id(), id()));
        assertEquals('z', e.converge(convergenceFn).match(id(), id(), id(), id(), id(), id()));
        assertEquals(5L, f.converge(convergenceFn).match(id(), id(), id(), id(), id(), id()));
        assertEquals(1, Choice7.<Integer, String, Boolean, Double, Character, Long, Float>g(1F).converge(convergenceFn).match(id(), id(), id(), id(), id(), id()));
        assertEquals("b", Choice7.<Integer, String, Boolean, Double, Character, Long, Float>g(2F).converge(convergenceFn).match(id(), id(), id(), id(), id(), id()));
        assertEquals(false, Choice7.<Integer, String, Boolean, Double, Character, Long, Float>g(3F).converge(convergenceFn).match(id(), id(), id(), id(), id(), id()));
        assertEquals(1d, Choice7.<Integer, String, Boolean, Double, Character, Long, Float>g(4F).converge(convergenceFn).match(id(), id(), id(), id(), id(), id()));
        assertEquals('a', Choice7.<Integer, String, Boolean, Double, Character, Long, Float>g(5F).converge(convergenceFn).match(id(), id(), id(), id(), id(), id()));
        assertEquals(5L, Choice7.<Integer, String, Boolean, Double, Character, Long, Float>g(6F).converge(convergenceFn).match(id(), id(), id(), id(), id(), id()));
    }

    @Test
    public void projections() {
        assertEquals(tuple(just(1), nothing(), nothing(), nothing(), nothing(), nothing(), nothing()), a.project());
        assertEquals(tuple(nothing(), just("two"), nothing(), nothing(), nothing(), nothing(), nothing()), b.project());
        assertEquals(tuple(nothing(), nothing(), just(true), nothing(), nothing(), nothing(), nothing()), c.project());
        assertEquals(tuple(nothing(), nothing(), nothing(), just(4D), nothing(), nothing(), nothing()), d.project());
        assertEquals(tuple(nothing(), nothing(), nothing(), nothing(), just('z'), nothing(), nothing()), e.project());
        assertEquals(tuple(nothing(), nothing(), nothing(), nothing(), nothing(), just(5L), nothing()), f.project());
        assertEquals(tuple(nothing(), nothing(), nothing(), nothing(), nothing(), nothing(), just(6F)), g.project());

        assertEquals(tuple(a.projectA(), a.projectB(), a.projectC(), a.projectD(), a.projectE(), a.projectF(), a.projectG()), a.project());
        assertEquals(tuple(b.projectA(), b.projectB(), b.projectC(), b.projectD(), b.projectE(), b.projectF(), b.projectG()), b.project());
        assertEquals(tuple(c.projectA(), c.projectB(), c.projectC(), c.projectD(), c.projectE(), c.projectF(), c.projectG()), c.project());
        assertEquals(tuple(d.projectA(), d.projectB(), d.projectC(), d.projectD(), d.projectE(), d.projectF(), d.projectG()), d.project());
        assertEquals(tuple(e.projectA(), e.projectB(), e.projectC(), e.projectD(), e.projectE(), e.projectF(), e.projectG()), e.project());
        assertEquals(tuple(f.projectA(), f.projectB(), f.projectC(), f.projectD(), f.projectE(), f.projectF(), f.projectG()), f.project());
        assertEquals(tuple(g.projectA(), g.projectB(), g.projectC(), g.projectD(), g.projectE(), g.projectF(), g.projectG()), g.project());
    }

    @Test
    public void embed() {
        assertEquals(just(a), a.embed(Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just));
        assertEquals(just(b), b.embed(Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just));
        assertEquals(just(c), c.embed(Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just));
        assertEquals(just(d), d.embed(Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just));
        assertEquals(just(e), e.embed(Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just));
        assertEquals(just(f), f.embed(Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just));
        assertEquals(just(g), g.embed(Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just));
    }
}