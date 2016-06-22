package com.jnape.palatable.lambda.adt.hlist;

import org.junit.Before;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.hlist.HList.nil;
import static org.junit.Assert.assertEquals;

public class SingletonTest {

    private Singleton<Integer> singleton;

    @Before
    public void setUp() {
        singleton = new Singleton<>(1);
    }

    @Test
    public void head() {
        assertEquals((Integer) 1, singleton.head());
    }

    @Test
    public void tail() {
        assertEquals(nil(), singleton.tail());
    }

    @Test
    public void cons() {
        assertEquals(new Tuple2<>("0", singleton), singleton.cons("0"));
    }

    @Test
    public void functorProperties() {
        assertEquals(new Singleton<>("1"), singleton.fmap(Object::toString));
    }
}