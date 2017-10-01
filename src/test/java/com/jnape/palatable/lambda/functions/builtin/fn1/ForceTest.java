package com.jnape.palatable.lambda.functions.builtin.fn1;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Force.force;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class ForceTest {

    @Test
    public void performsAnySideEffects() {
        AtomicInteger counter = new AtomicInteger();
        Iterable<Integer> ints = map(x -> {
            counter.incrementAndGet();
            return x;
        }, asList(1, 2, 3));

        assertEquals(0, counter.get());

        force(ints);
        force(ints);

        assertEquals(6, counter.get());
    }
}