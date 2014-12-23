package com.jnape.palatable.lambda.functions;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TriadicFunctionTest {

    private static final TriadicFunction<Integer, Integer, Integer, Boolean> CHECK_MULTIPLICATION =
            (multiplicand, multiplier, guessResult) -> multiplicand * multiplier == guessResult;

    @Test
    public void canBePartiallyApplied() {
        DyadicFunction<Integer, Integer, Boolean> checkMultiplicationBy2 = CHECK_MULTIPLICATION.apply(2);
        assertThat(checkMultiplicationBy2.apply(4, 8), is(true));
    }
}
