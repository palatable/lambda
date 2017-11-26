package com.jnape.palatable.lambda.adt.coproduct;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.choice.Choice4;
import com.jnape.palatable.lambda.adt.choice.Choice5;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static org.junit.Assert.assertEquals;

public class CoProduct5Test {

    private CoProduct5<Integer, String, Boolean, Double, Character, ?> a;
    private CoProduct5<Integer, String, Boolean, Double, Character, ?> b;
    private CoProduct5<Integer, String, Boolean, Double, Character, ?> c;
    private CoProduct5<Integer, String, Boolean, Double, Character, ?> d;
    private CoProduct5<Integer, String, Boolean, Double, Character, ?> e;

    @Before
    public void setUp() {
        a = new CoProduct5<Integer, String, Boolean, Double, Character, CoProduct5<Integer, String, Boolean, Double, Character, ?>>() {
            @Override
            public <R> R match(Function<? super Integer, ? extends R> aFn, Function<? super String, ? extends R> bFn,
                               Function<? super Boolean, ? extends R> cFn, Function<? super Double, ? extends R> dFn,
                               Function<? super Character, ? extends R> eFn) {
                return aFn.apply(1);
            }
        };
        b = new CoProduct5<Integer, String, Boolean, Double, Character, CoProduct5<Integer, String, Boolean, Double, Character, ?>>() {
            @Override
            public <R> R match(Function<? super Integer, ? extends R> aFn, Function<? super String, ? extends R> bFn,
                               Function<? super Boolean, ? extends R> cFn, Function<? super Double, ? extends R> dFn,
                               Function<? super Character, ? extends R> eFn) {
                return bFn.apply("two");
            }
        };
        c = new CoProduct5<Integer, String, Boolean, Double, Character, CoProduct5<Integer, String, Boolean, Double, Character, ?>>() {
            @Override
            public <R> R match(Function<? super Integer, ? extends R> aFn, Function<? super String, ? extends R> bFn,
                               Function<? super Boolean, ? extends R> cFn, Function<? super Double, ? extends R> dFn,
                               Function<? super Character, ? extends R> eFn) {
                return cFn.apply(true);
            }
        };
        d = new CoProduct5<Integer, String, Boolean, Double, Character, CoProduct5<Integer, String, Boolean, Double, Character, ?>>() {
            @Override
            public <R> R match(Function<? super Integer, ? extends R> aFn, Function<? super String, ? extends R> bFn,
                               Function<? super Boolean, ? extends R> cFn, Function<? super Double, ? extends R> dFn,
                               Function<? super Character, ? extends R> eFn) {
                return dFn.apply(4d);
            }
        };
        e = new CoProduct5<Integer, String, Boolean, Double, Character, CoProduct5<Integer, String, Boolean, Double, Character, ?>>() {
            @Override
            public <R> R match(Function<? super Integer, ? extends R> aFn, Function<? super String, ? extends R> bFn,
                               Function<? super Boolean, ? extends R> cFn, Function<? super Double, ? extends R> dFn,
                               Function<? super Character, ? extends R> eFn) {
                return eFn.apply('z');
            }
        };
    }

    @Test
    public void match() {
        assertEquals(1, a.match(id(), id(), id(), id(), id()));
        assertEquals("two", b.match(id(), id(), id(), id(), id()));
        assertEquals(true, c.match(id(), id(), id(), id(), id()));
        assertEquals(4D, d.match(id(), id(), id(), id(), id()));
        assertEquals('z', e.match(id(), id(), id(), id(), id()));
    }

    @Test
    public void diverge() {
        assertEquals(1, a.diverge().match(id(), id(), id(), id(), id(), id()));
        assertEquals("two", b.diverge().match(id(), id(), id(), id(), id(), id()));
        assertEquals(true, c.diverge().match(id(), id(), id(), id(), id(), id()));
        assertEquals(4D, d.diverge().match(id(), id(), id(), id(), id(), id()));
        assertEquals('z', e.diverge().match(id(), id(), id(), id(), id(), id()));
    }

    @Test
    public void converge() {
        Function<Character, CoProduct4<Integer, String, Boolean, Double, ?>> convergenceFn = x -> x.equals('a')
                                                                                                  ? Choice4.a(1)
                                                                                                  : x.equals('b')
                                                                                                    ? Choice4.b("b")
                                                                                                    : x.equals('c')
                                                                                                      ? Choice4.c(false)
                                                                                                      : Choice4.d(1D);
        assertEquals(1, a.converge(convergenceFn).match(id(), id(), id(), id()));
        assertEquals("two", b.converge(convergenceFn).match(id(), id(), id(), id()));
        assertEquals(true, c.converge(convergenceFn).match(id(), id(), id(), id()));
        assertEquals(4D, d.converge(convergenceFn).match(id(), id(), id(), id()));
        assertEquals(1, Choice5.<Integer, String, Boolean, Double, Character>e('a').converge(convergenceFn).match(id(), id(), id(), id()));
        assertEquals("b", Choice5.<Integer, String, Boolean, Double, Character>e('b').converge(convergenceFn).match(id(), id(), id(), id()));
        assertEquals(false, Choice5.<Integer, String, Boolean, Double, Character>e('c').converge(convergenceFn).match(id(), id(), id(), id()));
        assertEquals(1d, Choice5.<Integer, String, Boolean, Double, Character>e('d').converge(convergenceFn).match(id(), id(), id(), id()));
    }

    @Test
    public void projections() {
        assertEquals(tuple(just(1), nothing(), nothing(), nothing(), nothing()), a.project());
        assertEquals(tuple(nothing(), just("two"), nothing(), nothing(), nothing()), b.project());
        assertEquals(tuple(nothing(), nothing(), just(true), nothing(), nothing()), c.project());
        assertEquals(tuple(nothing(), nothing(), nothing(), just(4D), nothing()), d.project());
        assertEquals(tuple(nothing(), nothing(), nothing(), nothing(), just('z')), e.project());

        assertEquals(tuple(a.projectA(), a.projectB(), a.projectC(), a.projectD(), a.projectE()), a.project());
        assertEquals(tuple(b.projectA(), b.projectB(), b.projectC(), b.projectD(), b.projectE()), b.project());
        assertEquals(tuple(c.projectA(), c.projectB(), c.projectC(), c.projectD(), c.projectE()), c.project());
        assertEquals(tuple(d.projectA(), d.projectB(), d.projectC(), d.projectD(), d.projectE()), d.project());
        assertEquals(tuple(e.projectA(), e.projectB(), e.projectC(), e.projectD(), e.projectE()), e.project());
    }

    @Test
    public void embed() {
        assertEquals(just(a), a.embed(Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just));
        assertEquals(just(b), b.embed(Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just));
        assertEquals(just(c), c.embed(Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just));
        assertEquals(just(d), d.embed(Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just));
        assertEquals(just(e), e.embed(Maybe::just, Maybe::just, Maybe::just, Maybe::just, Maybe::just));
    }
}