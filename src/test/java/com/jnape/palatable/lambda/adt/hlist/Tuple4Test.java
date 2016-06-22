package com.jnape.palatable.lambda.adt.hlist;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Tuple4Test {

    private Tuple4<Integer, String, Character, Boolean> tuple4;

    @Before
    public void setUp() {
        tuple4 = new Tuple4<>(1, new Tuple3<>("2", new Tuple2<>('3', new Singleton<>(false))));
    }

    @Test
    public void head() {
        assertEquals((Integer) 1, tuple4.head());
    }

    @Test
    public void tail() {
        assertEquals(new Tuple3<>("2", new Tuple2<>('3', new Singleton<>(false))), tuple4.tail());
    }

    @Test
    public void cons() {
        assertEquals(new Tuple5<>(0, tuple4), tuple4.cons(0));
    }

    @Test
    public void accessors() {
        assertEquals((Integer) 1, tuple4._1());
        assertEquals("2", tuple4._2());
        assertEquals((Character) '3', tuple4._3());
        assertEquals(false, tuple4._4());
    }

    @Test
    public void functorProperties() {
        assertEquals(new Tuple4<>(1, new Tuple3<>("2", new Tuple2<>('3', new Singleton<>(true)))), tuple4.fmap(x -> !x));
    }

    @Test
    public void bifunctorProperties() {
        assertEquals(new Tuple4<>(1, new Tuple3<>("2", new Tuple2<>("3", new Singleton<>(true)))),
                     tuple4.biMap(Object::toString, x -> !x));
    }
}