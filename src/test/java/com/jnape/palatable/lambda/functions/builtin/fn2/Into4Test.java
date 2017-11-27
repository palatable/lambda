package com.jnape.palatable.lambda.functions.builtin.fn2;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into4.into4;
import static org.junit.Assert.assertEquals;

public class Into4Test {

    @Test
    public void appliesTupleToFunction() {
        assertEquals((Integer) 10, into4((a, b, c, d) -> a + b + c + d, tuple(1, 2, 3, 4)));
    }
}