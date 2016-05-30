package com.jnape.palatable.lambda.adt;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.HList.cons;
import static com.jnape.palatable.lambda.adt.HList.list;
import static com.jnape.palatable.lambda.adt.HList.nil;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class HListTest {

    @Test
    public void head() {
        assertEquals("head", nil().cons("head").head());
        assertEquals("new head", nil().cons("old head").cons("new head").head());
    }

    @Test
    public void tail() {
        assertEquals(nil(), nil().cons("head").tail());
        assertEquals(nil().cons("old head"), nil().cons("old head").cons("new head").tail());
    }

    @Test
    public void convenienceStaticFactoryMethods() {
        assertEquals(nil().cons(1), cons(1, nil()));
        assertEquals(nil().cons(1), list(1));
        assertEquals(nil().cons('2').cons(1), list(1, '2'));
        assertEquals(nil().cons("3").cons('2').cons(1), list(1, '2', "3"));
        assertEquals(nil().cons(4.0).cons("3").cons('2').cons(1), list(1, '2', "3", 4.0));
        assertEquals(nil().cons(false).cons(4.0).cons("3").cons('2').cons(1), list(1, '2', "3", 4.0, false));
    }

    @Test
    public void functorProperties() {
        assertEquals(list("1"), list(1).fmap(Object::toString));
    }

    @Test
    public void nilReusesInstance() {
        assertSame(nil(), nil());
    }
}