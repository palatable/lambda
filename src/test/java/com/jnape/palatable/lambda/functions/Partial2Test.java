package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.DyadicFunction;
import com.jnape.palatable.lambda.MonadicFunction;
import com.jnape.palatable.lambda.tuples.Tuple2;
import org.junit.Test;

import static com.jnape.palatable.lambda.functions.Partial2.partial2;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class Partial2Test {

    @Test
    public void partiallyAppliesFunction() {
        MonadicFunction<Tuple2<Integer, Integer>, Integer> subtract = new DyadicFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer minuend, Integer subtrahend) {
                return minuend - subtrahend;
            }
        };

        assertThat(partial2(subtract, 3).apply(2), is(1));
    }
}
