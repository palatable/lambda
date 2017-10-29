package com.jnape.palatable.lambda.monoid.builtin;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.monoid.builtin.Last.last;
import static org.junit.Assert.assertEquals;

public class LastTest {

    @Test
    public void identity() {
        assertEquals(nothing(), last().identity());
    }

    @Test
    public void monoid() {
        Last<Integer> last = last();
        assertEquals(just(2), last.apply(just(1), just(2)));
        assertEquals(just(2), last.apply(nothing(), just(2)));
        assertEquals(just(1), last.apply(just(1), nothing()));
        assertEquals(nothing(), last.apply(nothing(), nothing()));
    }
}