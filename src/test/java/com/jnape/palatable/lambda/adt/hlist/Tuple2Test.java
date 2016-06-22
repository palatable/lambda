package com.jnape.palatable.lambda.adt.hlist;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Tuple2Test {

    private Tuple2<Integer, Integer> tuple2;

    @Before
    public void setUp() throws Exception {
        tuple2 = new Tuple2<>(1, new Singleton<>(2));
    }

    @Test
    public void head() {
        assertEquals((Integer) 1, tuple2.head());
    }

    @Test
    public void tail() {
        assertEquals(new Singleton<>(2), tuple2.tail());
    }

    @Test
    public void cons() {
        assertEquals(new Tuple3<>(0, tuple2), tuple2.cons(0));
    }

    @Test
    public void accessors() {
        assertEquals((Integer) 1, tuple2._1());
        assertEquals((Integer) 2, tuple2._2());
    }

    @Test
    public void functorProperties() {
        assertEquals(new Tuple2<>(1, new Singleton<>("2")), tuple2.fmap(Object::toString));
    }

    @Test
    public void bifunctorProperties() {
        assertEquals(new Tuple2<>("1", new Singleton<>("2")), tuple2.biMap(Object::toString, Object::toString));
    }
}