package com.jnape.palatable.lambda.adt.hlist;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Tuple3Test {

    private Tuple3<Integer, String, Character> tuple3;

    @Before
    public void setUp() {
        tuple3 = new Tuple3<>(1, new Tuple2<>("2", new Singleton<>('3')));
    }

    @Test
    public void head() {
        assertEquals((Integer) 1, tuple3.head());
    }

    @Test
    public void tail() {
        assertEquals(new Tuple2<>("2", new Singleton<>('3')), tuple3.tail());
    }

    @Test
    public void cons() {
        assertEquals(new Tuple4<>(0, tuple3), tuple3.cons(0));
    }

    @Test
    public void accessors() {
        assertEquals((Integer) 1, tuple3._1());
        assertEquals("2", tuple3._2());
        assertEquals((Character) '3', tuple3._3());
    }

    @Test
    public void functorProperties() {
        assertEquals(new Tuple3<>(1, new Tuple2<>("2", new Singleton<>("3"))), tuple3.fmap(Object::toString));
    }

    @Test
    public void bifunctorProperties() {
        assertEquals(new Tuple3<>(1, new Tuple2<>(2, new Singleton<>("3"))), tuple3.biMap(Integer::parseInt, Object::toString));
    }
}
