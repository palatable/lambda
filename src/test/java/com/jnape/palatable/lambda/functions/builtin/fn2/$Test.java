package com.jnape.palatable.lambda.functions.builtin.fn2;

import org.junit.Test;

import static com.jnape.palatable.lambda.functions.Fn2.fn2;
import static com.jnape.palatable.lambda.functions.builtin.fn2.$.$;
import static org.junit.Assert.assertEquals;

public class $Test {

    @Test
    public void application() {
        assertEquals((Integer) 1, $(x -> x + 1, 0));
        assertEquals((Integer) 1, $.<Integer, Integer>$(x -> x + 1).apply(0));
    }

    @Test
    public void curryingInference() {
        assertEquals((Integer) 1, $($(fn2(Integer::sum), 0), 1));
    }
}