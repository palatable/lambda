package com.jnape.palatable.lambda.functions;

import org.junit.Test;

import static com.jnape.palatable.lambda.functions.ReduceRight.reduceRight;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static testsupport.functions.ExplainFold.explainFold;

public class ReduceRightTest {

    @Test
    public void reduceRightAccumulatesRightToLeftUsingLastElementAsStartingAccumulation() {
        assertThat(
                reduceRight(explainFold(), asList("1", "2", "3", "4", "5")),
                is("(1 + (2 + (3 + (4 + 5))))")
        );
    }
}
