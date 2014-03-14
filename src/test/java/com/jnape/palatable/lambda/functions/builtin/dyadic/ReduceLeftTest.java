package com.jnape.palatable.lambda.functions.builtin.dyadic;

import com.jnape.palatable.lambda.exceptions.EmptyIterableException;
import org.junit.Test;

import java.util.Collections;

import static com.jnape.palatable.lambda.functions.builtin.dyadic.ReduceLeft.reduceLeft;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static testsupport.functions.ExplainFold.explainFold;

public class ReduceLeftTest {

    @Test
    public void reduceLeftAccumulatesLeftToRightUsingFirstElementAsStartingAccumulation() {
        assertThat(
                reduceLeft(explainFold(), asList("1", "2", "3", "4", "5")),
                is("((((1 + 2) + 3) + 4) + 5)")
        );
    }

    @Test(expected = EmptyIterableException.class)
    public void reduceLeftFailsIfEmptyIterable() {
        reduceLeft(explainFold(), Collections.<String>emptyList());
    }
}
