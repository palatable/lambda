package com.jnape.palatable.lambda.functions.builtin.fn2;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Find.find;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Iterate.iterate;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class FindTest {

    @Test
    public void findsFirstElementMatchingPredicate() {
        assertEquals(just("three"), find(s -> s.length() > 3, asList("one", "two", "three", "four")));
    }

    @Test
    public void isEmptyIfNoElementsMatchPredicate() {
        assertEquals(nothing(), find(s -> s.length() > 5, asList("one", "two", "three", "four")));
    }

    @Test
    public void shortCircuitsOnMatch() {
        assertEquals(just(0), find(constantly(true), iterate(x -> x + 1, 0)));
    }
}