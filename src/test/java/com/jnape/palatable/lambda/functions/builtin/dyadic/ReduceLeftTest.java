package com.jnape.palatable.lambda.functions.builtin.dyadic;

import org.junit.Test;

import java.util.Optional;

import static com.jnape.palatable.lambda.functions.builtin.dyadic.ReduceLeft.reduceLeft;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static testsupport.functions.ExplainFold.explainFold;

public class ReduceLeftTest {

    @Test
    public void reduceLeftAccumulatesLeftToRightUsingFirstElementAsStartingAccumulation() {
        assertThat(
                reduceLeft(explainFold(), asList("1", "2", "3", "4", "5")),
                is(Optional.of("((((1 + 2) + 3) + 4) + 5)"))
        );
    }

    @Test
    public void isEmptyIfIterableIsEmpty() {
        assertThat(
                reduceLeft(explainFold(), emptyList()),
                is(Optional.empty())
        );
    }
}
