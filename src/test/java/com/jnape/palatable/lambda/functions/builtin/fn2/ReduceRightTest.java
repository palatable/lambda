package com.jnape.palatable.lambda.functions.builtin.fn2;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn2.ReduceRight.reduceRight;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static testsupport.functions.ExplainFold.explainFold;

public class ReduceRightTest {

    @Test
    public void accumulatesRightToLeftUsingLastElementAsStartingAccumulation() {
        assertThat(reduceRight(explainFold(), asList("1", "2", "3", "4", "5")), is(just("(1 + (2 + (3 + (4 + 5))))")));
    }

    @Test
    public void isEmptyIfIterableIsEmpty() {
        assertThat(reduceRight(explainFold(), emptyList()), is(nothing()));
    }
}
