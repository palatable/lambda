package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Head.head;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;

@RunWith(Traits.class)
public class HeadTest {

    @Test
    public void returnsTheHeadOfNonEmptyIterable() {
        assertEquals(just(1), head(asList(1, 2, 3)));
    }

    @Test
    public void isEmptyForEmptyIterable() {
        assertEquals(nothing(), head(emptyList()));
    }
}