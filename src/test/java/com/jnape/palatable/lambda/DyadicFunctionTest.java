package com.jnape.palatable.lambda;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DyadicFunctionTest {

    @Test
    public void flipSwapsArguments() {
        DyadicFunction<String, Integer, Boolean> checkLength = new DyadicFunction<String, Integer, Boolean>() {
            @Override
            public Boolean apply(String string, Integer length) {
                return string.length() == length;
            }
        };

        assertThat(checkLength.flip().apply(3, "foo"), is(true));
    }
}
