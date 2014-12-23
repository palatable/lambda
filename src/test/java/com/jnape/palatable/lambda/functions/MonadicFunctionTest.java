package com.jnape.palatable.lambda.functions;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class MonadicFunctionTest {

    @Test
    public void composesByApplyingFunctionThenForwardingToNextFunction() {
        MonadicFunction<Integer, Integer> add2 = integer -> integer + 2;
        MonadicFunction<Integer, String> toString = Object::toString;

        assertThat(add2.then(toString).apply(2), is(toString.apply(add2.apply(2))));
    }
}
