package com.jnape.palatable.lambda.adt.hlist;

import com.jnape.palatable.lambda.adt.hlist.HList.HCons;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Tuple5Test {

    private Tuple5<Integer, String, Character, Boolean, Long> tuple5;

    @Before
    public void setUp() {
        tuple5 = new Tuple5<>(1, new Tuple4<>("2", new Tuple3<>('3', new Tuple2<>(false, new SingletonHList<>(5L)))));
    }

    @Test
    public void head() {
        assertEquals((Integer) 1, tuple5.head());
    }

    @Test
    public void tail() {
        assertEquals(new Tuple4<>("2", new Tuple3<>('3', new Tuple2<>(false, new SingletonHList<>(5L)))), tuple5.tail());
    }

    @Test
    public void cons() {
        assertEquals(new HCons<>(0, tuple5), tuple5.cons(0));
    }

    @Test
    public void accessors() {
        assertEquals((Integer) 1, tuple5._1());
        assertEquals("2", tuple5._2());
        assertEquals((Character) '3', tuple5._3());
        assertEquals(false, tuple5._4());
        assertEquals((Long) 5L, tuple5._5());
    }

    @Test
    public void functorProperties() {
        assertEquals(new Tuple5<>(1, new Tuple4<>("2", new Tuple3<>('3', new Tuple2<>(false, new SingletonHList<>("5"))))),
                     tuple5.fmap(Object::toString));
    }

    @Test
    public void bifunctorProperties() {
        assertEquals(new Tuple5<>(1, new Tuple4<>("2", new Tuple3<>('3', new Tuple2<>(true, new SingletonHList<>("5"))))),
                     tuple5.biMap(x -> !x, Object::toString));
    }
}