package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.semigroup.Semigroup;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.monoid.builtin.Present.present;
import static org.junit.Assert.assertEquals;

public class PresentTest {

    @Test
    public void monoid() {
        Present<Integer>   present  = present();
        Semigroup<Integer> addition = Integer::sum;

        assertEquals(just(3), present.apply(addition, just(1), just(2)));
        assertEquals(just(1), present.apply(addition, nothing(), just(1)));
        assertEquals(just(1), present.apply(addition, just(1), nothing()));
        assertEquals(nothing(), present.apply(addition, nothing(), nothing()));
    }
}