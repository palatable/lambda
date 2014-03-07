package com.jnape.palatable.lambda.tuples;

import org.junit.Test;

import static com.jnape.palatable.lambda.tuples.Tuple3.tuple;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class Tuple3Test {

    @Test
    public void hasThreeSlots() {
        Tuple3<Integer, String, Character> integerStringCharacterTuple = tuple(1, "two", '3');

        assertThat(integerStringCharacterTuple._1, is(1));
        assertThat(integerStringCharacterTuple._2, is("two"));
        assertThat(integerStringCharacterTuple._3, is('3'));
    }
}
