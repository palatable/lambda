package com.jnape.palatable.lambda.functions;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DyadicFunctionTest {

    private static final DyadicFunction<String, Integer, Boolean> CHECK_LENGTH = (string,
                                                                                  length) -> string.length() == length;

    @Test
    public void flipSwapsArguments() {
        assertThat(CHECK_LENGTH.flip().apply(3, "foo"), is(true));
    }

    @Test
    public void canBePartiallyApplied() {
        MonadicFunction<Integer, Boolean> checkQuuxLength = CHECK_LENGTH.apply("quux");
        assertThat(checkQuuxLength.apply(4), is(true));
    }
}
