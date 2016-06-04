package com.jnape.palatable.lambda.functions.builtin.dyadic;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.tuples.Tuple2.tuple;
import static com.jnape.palatable.lambda.functions.builtin.dyadic.Tupler2.tupler;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class Tupler2Test {

    @Test
    public void createsTupleOfTwoThings() {
        assertThat(tupler("a", 1), is(tuple("a", 1)));
    }
}
