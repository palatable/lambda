package com.jnape.palatable.lambda.adt.hlist;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
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

    @Test
    public void mapEntryProperties() {
        assertEquals((Integer) 1, tuple2.getKey());
        assertEquals((Integer) 2, tuple2.getValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void setValueIsNotSupported() {
        tuple2.setValue(3);
    }

    @Test
    public void staticFactoryMethodFromMapEntry() {
        Map.Entry<String, Integer> stringIntEntry = new HashMap<String, Integer>() {{
            put("string", 1);
        }}.entrySet().iterator().next();

        assertEquals(tuple("string", 1), Tuple2.fromEntry(stringIntEntry));
    }
}