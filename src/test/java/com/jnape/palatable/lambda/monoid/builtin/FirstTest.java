package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.adt.Maybe;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Repeat.repeat;
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

    @Test(timeout = 500)
    public void shortCircuiting() {
        Iterable<Maybe<Integer>> maybeInts = repeat(just(1));
        First<Integer>           first     = First.first();

        assertEquals(just(1), first.foldLeft(nothing(), maybeInts));
        assertEquals(just(1), first.foldLeft(just(1), maybeInts));
        assertEquals(just(1), first.reduceLeft(maybeInts));
        assertEquals(just(1), first.foldMap(id(), maybeInts));
    }
}