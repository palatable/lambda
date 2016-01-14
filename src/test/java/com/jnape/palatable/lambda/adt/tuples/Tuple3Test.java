package com.jnape.palatable.lambda.adt.tuples;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.tuples.Tuple3.tuple;
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

    @Test
    public void toStringIsReasonable() {
        assertThat(tuple("foo", "bar", "baz").toString(), is("(foo, bar, baz)"));
    }
}
