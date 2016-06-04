package com.jnape.palatable.lambda.adt.tuples;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.tuples.Tuple3.tuple;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
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
    public void usesValueBasedEquality() {
        Tuple3<String, String, Integer> same = tuple("a", "b", 1);
        Tuple3<String, String, Integer> alsoSame = tuple("a", "b", 1);
        Tuple3<String, String, Integer> different = tuple("b", "b", 2);

        assertEquals(same, alsoSame);
        assertEquals(alsoSame, same);

        assertNotEquals(same, different);
        assertNotEquals(alsoSame, different);
        assertNotEquals(different, same);
        assertNotEquals(different, alsoSame);

        assertNotEquals(same, new Object());
    }

    @Test
    public void hashesCorrectlyForEqualTuples() {
        assertEquals(tuple("foo", "bar", 1).hashCode(), tuple("foo", "bar", 1).hashCode());
    }

    @Test
    public void hashesUsingReasonableDistribution() {
        assertNotEquals(tuple("foo", "bar", 1).hashCode(), tuple("bar", "baz", 2).hashCode());
    }

    @Test
    public void toStringIsReasonable() {
        assertThat(tuple("foo", "bar", "baz").toString(), is("(foo, bar, baz)"));
    }
}
