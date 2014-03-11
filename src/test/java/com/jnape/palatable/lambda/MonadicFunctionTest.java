package com.jnape.palatable.lambda;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class MonadicFunctionTest {

    @Test
    public void composesByApplyingFunctionThenForwardingToNextFunction() {
        MonadicFunction<Integer, Integer> add2 = new MonadicFunction<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                return integer + 2;
            }
        };
        MonadicFunction<Integer, String> toString = new MonadicFunction<Integer, String>() {
            @Override
            public String apply(Integer integer) {
                return integer.toString();
            }
        };

        assertThat(add2.then(toString).apply(2), is(toString.apply(add2.apply(2))));
    }
}
