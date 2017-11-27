package com.jnape.palatable.lambda.functions.builtin.fn2;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into8.into8;
import static org.junit.Assert.assertEquals;

public class Into8Test {

    @Test
    public void appliesTupleToFunction() {
        assertEquals((Integer) 36, into8((a, b, c, d, e, f, g, h) -> a + b + c + d + e + f + g + h, tuple(1, 2, 3, 4, 5, 6, 7, 8)));
    }
}