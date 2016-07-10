package com.jnape.palatable.lambda.adt.hlist;

import org.junit.Before;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.hlist.HList.nil;
import static org.junit.Assert.assertEquals;

public class SingletonHListTest {

    private SingletonHList<Integer> singletonHList;

    @Before
    public void setUp() {
        singletonHList = new SingletonHList<>(1);
    }

    @Test
    public void head() {
        assertEquals((Integer) 1, singletonHList.head());
    }

    @Test
    public void tail() {
        assertEquals(nil(), singletonHList.tail());
    }

    @Test
    public void cons() {
        assertEquals(new Tuple2<>("0", singletonHList), singletonHList.cons("0"));
    }

    @Test
    public void functorProperties() {
        assertEquals(new SingletonHList<>("1"), singletonHList.fmap(Object::toString));
    }
}