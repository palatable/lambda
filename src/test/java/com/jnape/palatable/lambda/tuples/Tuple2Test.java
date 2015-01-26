package com.jnape.palatable.lambda.tuples;

import org.junit.Test;

import static com.jnape.palatable.lambda.tuples.Tuple2.tuple;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class Tuple2Test {

    @Test
    public void hasTwoSlots() {
        Tuple2<String, Integer> stringIntTuple = new Tuple2<>("foo", 1);

        assertThat(stringIntTuple._1, is("foo"));
        assertThat(stringIntTuple._2, is(1));
    }

    @Test
    public void biMapMapsOverBothSlots() {
        assertThat(
                tuple("foo", 1).biMap(String::toUpperCase, i -> i + 1),
                is(tuple("FOO", 2))
        );
    }
}
