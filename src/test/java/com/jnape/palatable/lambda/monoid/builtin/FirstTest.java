package com.jnape.palatable.lambda.monoid.builtin;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.monoid.builtin.First.first;
import static org.junit.Assert.assertEquals;

public class FirstTest {

    @Test
    public void identity() {
        assertEquals(nothing(), first().identity());
    }

    @Test
    public void monoid() {
        First<Integer> first = first();
        assertEquals(just(1), first.apply(just(1), just(2)));
        assertEquals(just(1), first.apply(just(1), nothing()));
        assertEquals(just(2), first.apply(nothing(), just(2)));
        assertEquals(nothing(), first.apply(nothing(), nothing()));
    }
}