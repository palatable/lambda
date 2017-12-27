package com.jnape.palatable.lambda.functions.builtin.fn2;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.hlist.HList.singletonHList;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into1.into1;
import static org.junit.Assert.assertEquals;

public class Into1Test {

    @Test
    public void appliesSingletonHListHeadToFunction() {
        assertEquals("FOO", into1(String::toUpperCase, singletonHList("foo")));
    }
}