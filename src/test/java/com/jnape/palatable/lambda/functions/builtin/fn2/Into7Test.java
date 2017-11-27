package com.jnape.palatable.lambda.functions.builtin.fn2;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into7.into7;
import static org.junit.Assert.assertEquals;

public class Into7Test {

    @Test
    public void appliesTupleToFunction() {
        assertEquals((Integer) 28, into7((a, b, c, d, e, f, g) -> a + b + c + d + e + f + g, tuple(1, 2, 3, 4, 5, 6, 7)));
    }
}