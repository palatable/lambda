package com.jnape.palatable.lambda.functions;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TriadicFunctionTest {

    private static final TriadicFunction<Integer, Integer, Integer, Boolean> CHECK_MULTIPLICATION =
            (multiplicand, multiplier, guessResult) -> multiplicand * multiplier == guessResult;

    @Test
    public void canBePartiallyApplied() {
        assertThat(CHECK_MULTIPLICATION.apply(2).apply(4).apply(8), is(true));
        assertThat(CHECK_MULTIPLICATION.apply(2).apply(4, 8), is(true));
        assertThat(CHECK_MULTIPLICATION.apply(2, 4).apply(8), is(true));
    }

    @Test
    public void uncurries() {
        assertThat(CHECK_MULTIPLICATION.uncurry().apply(tuple(2, 3), 6), is(true));
    }

    @Test
    public void flipsFirstAndSecondArgument() {
        TriadicFunction<String, Integer, String, String> concat3 = (a, b, c) -> a + b.toString() + c;
        assertThat(concat3.flip().apply(1, "2", "3"), is("213"));
    }
}
