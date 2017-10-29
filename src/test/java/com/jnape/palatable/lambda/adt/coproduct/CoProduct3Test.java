package com.jnape.palatable.lambda.adt.coproduct;

import com.jnape.palatable.lambda.adt.Maybe;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static org.junit.Assert.assertEquals;

public class CoProduct3Test {

    private CoProduct3<Integer, String, Boolean, ?> a;
    private CoProduct3<Integer, String, Boolean, ?> b;
    private CoProduct3<Integer, String, Boolean, ?> c;

    @Before
    public void setUp() {
        a = new CoProduct3<Integer, String, Boolean, CoProduct3<Integer, String, Boolean, ?>>() {
            @Override
            public <R> R match(Function<? super Integer, ? extends R> aFn, Function<? super String, ? extends R> bFn,
                               Function<? super Boolean, ? extends R> cFn) {
                return aFn.apply(1);
            }
        };
        b = new CoProduct3<Integer, String, Boolean, CoProduct3<Integer, String, Boolean, ?>>() {
            @Override
            public <R> R match(Function<? super Integer, ? extends R> aFn, Function<? super String, ? extends R> bFn,
                               Function<? super Boolean, ? extends R> cFn) {
                return bFn.apply("two");
            }
        };
        c = new CoProduct3<Integer, String, Boolean, CoProduct3<Integer, String, Boolean, ?>>() {
            @Override
            public <R> R match(Function<? super Integer, ? extends R> aFn, Function<? super String, ? extends R> bFn,
                               Function<? super Boolean, ? extends R> cFn) {
                return cFn.apply(true);
            }
        };
    }

    @Test
    public void match() {
        assertEquals(1, a.match(id(), id(), id()));
        assertEquals("two", b.match(id(), id(), id()));
        assertEquals(true, c.match(id(), id(), id()));
    }

    @Test
    public void diverge() {
        assertEquals(1, a.diverge().match(id(), id(), id(), id()));
        assertEquals("two", b.diverge().match(id(), id(), id(), id()));
        assertEquals(true, c.diverge().match(id(), id(), id(), id()));
    }

    @Test
    public void converge() {
        Function<Boolean, CoProduct2<Integer, String, ?>> convergenceFn = x -> x ? new CoProduct2<Integer, String, CoProduct2<Integer, String, ?>>() {
            @Override
            public <R> R match(Function<? super Integer, ? extends R> aFn, Function<? super String, ? extends R> bFn) {
                return aFn.apply(-1);
            }
        } : new CoProduct2<Integer, String, CoProduct2<Integer, String, ?>>() {
            @Override
            public <R> R match(Function<? super Integer, ? extends R> aFn, Function<? super String, ? extends R> bFn) {
                return bFn.apply("false");
            }
        };
        assertEquals(1, a.converge(convergenceFn).match(id(), id()));
        assertEquals("two", b.converge(convergenceFn).match(id(), id()));
        assertEquals(-1, c.converge(convergenceFn).match(id(), id()));
        assertEquals("false", new CoProduct3<Integer, String, Boolean, CoProduct3<Integer, String, Boolean, ?>>() {
            @Override
            public <R> R match(Function<? super Integer, ? extends R> aFn, Function<? super String, ? extends R> bFn,
                               Function<? super Boolean, ? extends R> cFn) {
                return cFn.apply(false);
            }
        }.converge(convergenceFn).match(id(), id()));
    }

    @Test
    public void projections() {
        assertEquals(tuple(just(1), nothing(), nothing()), a.project());
        assertEquals(tuple(nothing(), just("two"), nothing()), b.project());
        assertEquals(tuple(nothing(), nothing(), just(true)), c.project());

        assertEquals(tuple(a.projectA(), a.projectB(), a.projectC()), a.project());
        assertEquals(tuple(b.projectA(), b.projectB(), b.projectC()), b.project());
        assertEquals(tuple(c.projectA(), c.projectB(), c.projectC()), c.project());
    }

    @Test
    public void embed() {
        assertEquals(just(a), a.embed(Maybe::just, Maybe::just, Maybe::just));
        assertEquals(just(b), b.embed(Maybe::just, Maybe::just, Maybe::just));
        assertEquals(just(c), c.embed(Maybe::just, Maybe::just, Maybe::just));
    }
}