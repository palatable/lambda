package com.jnape.palatable.lambda.functions.builtin.fn2;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static org.junit.Assert.assertEquals;

public class IntoTest {

    @Test
    public void appliesTupleToFunction() {
        assertEquals((Integer) 3, into((a, b) -> a + b, tuple(1, 2)));
    }
}