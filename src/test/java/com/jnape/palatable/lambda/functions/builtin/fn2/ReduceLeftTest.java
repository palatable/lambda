package com.jnape.palatable.lambda.functions.builtin.fn2;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn2.ReduceLeft.reduceLeft;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static testsupport.functions.ExplainFold.explainFold;

public class ReduceLeftTest {

    @Test
    public void reduceLeftAccumulatesLeftToRightUsingFirstElementAsStartingAccumulation() {
        assertThat(reduceLeft(explainFold(), asList("1", "2", "3", "4", "5")), is(just("((((1 + 2) + 3) + 4) + 5)")));
    }

    @Test
    public void isEmptyIfIterableIsEmpty() {
        assertThat(reduceLeft(explainFold(), emptyList()), is(nothing()));
    }
}
