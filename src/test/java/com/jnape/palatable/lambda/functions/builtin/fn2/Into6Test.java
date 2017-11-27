package com.jnape.palatable.lambda.functions.builtin.fn2;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into6.into6;
import static org.junit.Assert.assertEquals;

public class Into6Test {

    @Test
    public void appliesTupleToFunction() {
        assertEquals((Integer) 21, into6((a, b, c, d, e, f) -> a + b + c + d + e + f, tuple(1, 2, 3, 4, 5, 6)));
    }
}