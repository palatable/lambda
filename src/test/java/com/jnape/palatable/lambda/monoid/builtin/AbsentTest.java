package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.semigroup.Semigroup;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.monoid.builtin.Absent.absent;
import static org.junit.Assert.assertEquals;

public class AbsentTest {

    @Test
    public void monoid() {
        Absent<Integer> absent = absent();
        Semigroup<Integer> addition = (x, y) -> x + y;

        assertEquals(just(3), absent.apply(addition, just(1), just(2)));
        assertEquals(nothing(), absent.apply(addition, nothing(), just(1)));
        assertEquals(nothing(), absent.apply(addition, just(1), nothing()));
        assertEquals(nothing(), absent.apply(addition, nothing(), nothing()));
    }
}