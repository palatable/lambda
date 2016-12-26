package com.jnape.palatable.lambda.adt.coproduct;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static org.junit.Assert.assertEquals;

public class CoProduct2Test {

    private CoProduct2<Integer, Boolean> a;
    private CoProduct2<Integer, Boolean> b;

    @Before
    public void setUp() {
        a = new CoProduct2<Integer, Boolean>() {
            @Override
            public <R> R match(Function<? super Integer, ? extends R> aFn, Function<? super Boolean, ? extends R> bFn) {
                return aFn.apply(1);
            }
        };
        b = new CoProduct2<Integer, Boolean>() {
            @Override
            public <R> R match(Function<? super Integer, ? extends R> aFn, Function<? super Boolean, ? extends R> bFn) {
                return bFn.apply(true);
            }
        };
    }

    @Test
    public void diverge() {
        CoProduct3<Integer, Boolean, String> divergeA = a.diverge();
        assertEquals(1, divergeA.match(id(), id(), id()));

        CoProduct3<Integer, Boolean, String> divergeB = b.diverge();
        assertEquals(true, divergeB.match(id(), id(), id()));
    }

    @Test
    public void projections() {
        assertEquals(tuple(Optional.of(1), Optional.empty()), a.project());
        assertEquals(tuple(Optional.empty(), Optional.of(true)), b.project());

        assertEquals(tuple(a.projectA(), a.projectB()), a.project());
        assertEquals(tuple(b.projectA(), b.projectB()), b.project());
    }
}