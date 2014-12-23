package com.jnape.palatable.lambda.tuples;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class Tuple2Test {

    @Test
    public void hasTwoSlots() {
        Tuple2<String, Integer> stringIntTuple = new Tuple2<>("foo", 1);

        assertThat(stringIntTuple._1, is("foo"));
        assertThat(stringIntTuple._2, is(1));
    }
}
