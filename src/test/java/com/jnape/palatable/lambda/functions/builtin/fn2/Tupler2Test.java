package com.jnape.palatable.lambda.functions.builtin.fn2;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Tupler2.tupler;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class Tupler2Test {

    @Test
    public void createsTupleOfTwoThings() {
        assertThat(tupler("a", 1), is(tuple("a", 1)));
    }
}
