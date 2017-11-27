package com.jnape.palatable.lambda.functions.builtin.fn2;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into3.into3;
import static org.junit.Assert.assertEquals;

public class Into3Test {

    @Test
    public void appliesTupleToFunction() {
        assertEquals((Integer) 6, into3((a, b, c) -> a + b + c, tuple(1, 2, 3)));
    }
}