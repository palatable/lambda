package com.jnape.palatable.lambda.functions.builtin.fn2;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into5.into5;
import static org.junit.Assert.assertEquals;

public class Into5Test {

    @Test
    public void appliesTupleToFunction() {
        assertEquals((Integer) 15, into5((a, b, c, d, e) -> a + b + c + d + e, tuple(1, 2, 3, 4, 5)));
    }
}