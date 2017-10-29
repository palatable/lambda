package com.jnape.palatable.lambda.functions.builtin.fn1;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Last.last;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;

public class LastTest {

    @Test
    public void presentForNonEmptyIterable() {
        assertEquals(just(3), last(asList(1, 2, 3)));
    }

    @Test
    public void emptyForEmptyIterables() {
        assertEquals(nothing(), last(emptyList()));
    }
}