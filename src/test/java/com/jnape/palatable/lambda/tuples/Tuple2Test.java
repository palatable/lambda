package com.jnape.palatable.lambda.tuples;

import org.junit.Test;

import static com.jnape.palatable.lambda.tuples.Tuple2.tuple;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

public class Tuple2Test {

    @Test
    public void hasTwoSlots() {
        Tuple2<String, Integer> stringIntTuple = new Tuple2<>("foo", 1);

        assertThat(stringIntTuple._1, is("foo"));
        assertThat(stringIntTuple._2, is(1));
    }

    @Test
    public void mapsCovariantlyOverOverLeftSlot() {
        assertThat(
                tuple("foo", 1).biMapL(String::toUpperCase),
                is(tuple("FOO", 1))
        );
    }

    @Test
    public void mapsCovariantlyOverRightSlot() {
        assertThat(
                tuple("foo", 1).biMapR(String::valueOf),
                is(tuple("foo", "1"))
        );
    }

    @Test
    public void mapsOverBothSlotsInSingleTransformation() {
        assertThat(
                tuple("foo", 1).biMap(String::toUpperCase, i -> i + 1),
                is(tuple("FOO", 2))
        );
    }

    @Test
    public void toStringIsReasonable() {
        assertThat(tuple("1", "2").toString(), is("(1, 2)"));
    }

    @Test
    public void usesValueBasedEquality() {
        Tuple2<String, Integer> same = tuple("a", 1);
        Tuple2<String, Integer> alsoSame = tuple("a", 1);
        Tuple2<String, Integer> different = tuple("b", 2);

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
        assertEquals(tuple("foo", 1).hashCode(), tuple("foo", 1).hashCode());
    }

    @Test
    public void hashesUsingReasonableDistribution() {
        assertNotEquals(tuple("foo", 1).hashCode(), tuple("bar", 2).hashCode());
    }
}
