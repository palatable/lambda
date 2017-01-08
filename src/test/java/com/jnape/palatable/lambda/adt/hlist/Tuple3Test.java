package com.jnape.palatable.lambda.adt.hlist;

import org.junit.Before;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class Tuple3Test {

    private Tuple3<Integer, String, Character> tuple3;

    @Before
    public void setUp() {
        tuple3 = new Tuple3<>(1, new Tuple2<>("2", new SingletonHList<>('3')));
    }

    @Test
    public void head() {
        assertEquals((Integer) 1, tuple3.head());
    }

    @Test
    public void tail() {
        assertEquals(new Tuple2<>("2", new SingletonHList<>('3')), tuple3.tail());
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
    public void randomAccess() {
        Tuple2<String, String> spiedTail = spy(tuple("second", "third"));
        Tuple3<String, String, String> tuple3 = new Tuple3<>("first", spiedTail);

        verify(spiedTail, times(1))._1();
        verify(spiedTail, times(1))._2();
        tuple3._1();
        tuple3._2();
        tuple3._3();
        verifyNoMoreInteractions(spiedTail);
    }

    @Test
    public void into() {
        Tuple3<String, Integer, Double> tuple = tuple("foo", 1, 2.0d);
        assertEquals("foo12.0", tuple.into((s, i, d) -> s + i + d));
    }

    @Test
    public void functorProperties() {
        assertEquals(new Tuple3<>(1, new Tuple2<>("2", new SingletonHList<>("3"))), tuple3.fmap(Object::toString));
    }

    @Test
    public void bifunctorProperties() {
        assertEquals(new Tuple3<>(1, new Tuple2<>(2, new SingletonHList<>("3"))), tuple3.biMap(Integer::parseInt, Object::toString));
    }
}
