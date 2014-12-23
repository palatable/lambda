package com.jnape.palatable.lambda.functions.builtin.dyadic;

import com.jnape.palatable.lambda.functions.DyadicFunction;
import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.dyadic.Partial2.partial2;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class Partial2Test {

    @Test
    public void partiallyAppliesFunction() {
        DyadicFunction<Integer, Integer, Integer> subtract = (minuend, subtrahend) -> minuend - subtrahend;

        assertThat(partial2(subtract, 3).apply(2), is(1));
    }
}
