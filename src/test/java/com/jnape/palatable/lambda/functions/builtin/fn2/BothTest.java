package com.jnape.palatable.lambda.functions.builtin.fn2;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Both.both;
import static org.junit.Assert.assertEquals;

public class BothTest {

    @Test
    public void duallyAppliesTwoFunctionsToSameInput() {
        assertEquals(tuple(1, -1), both(x -> x + 1, x -> x - 1, 0));
    }
}