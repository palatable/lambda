package com.jnape.palatable.lambda.adt.hlist;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.hlist.HList.cons;
import static com.jnape.palatable.lambda.adt.hlist.HList.nil;
import static com.jnape.palatable.lambda.adt.hlist.HList.singletonHList;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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
        assertEquals(nil().cons(1), singletonHList(1));
        assertEquals(nil().cons('2').cons(1), tuple(1, '2'));
        assertEquals(nil().cons("3").cons('2').cons(1), tuple(1, '2', "3"));
        assertEquals(nil().cons(4.0).cons("3").cons('2').cons(1), tuple(1, '2', "3", 4.0));
        assertEquals(nil().cons(false).cons(4.0).cons("3").cons('2').cons(1), tuple(1, '2', "3", 4.0, false));
    }

    @Test
    public void nilReusesInstance() {
        assertSame(nil(), nil());
    }

    @Test
    @SuppressWarnings({"EqualsWithItself", "EqualsBetweenInconvertibleTypes"})
    public void equality() {
        assertTrue(nil().equals(nil()));
        assertTrue(cons(1, nil()).equals(cons(1, nil())));

        assertFalse(cons(1, nil()).equals(nil()));
        assertFalse(nil().equals(cons(1, nil())));

        assertFalse(cons(1, cons(2, nil())).equals(cons(1, nil())));
        assertFalse(cons(1, nil()).equals(cons(1, cons(2, nil()))));
    }

    @Test
    public void hashCodeUsesDecentDistribution() {
        assertEquals(nil().hashCode(), nil().hashCode());
        assertEquals(nil().cons(1).hashCode(), nil().cons(1).hashCode());

        assertNotEquals(nil().cons(1).hashCode(), nil().cons(2).hashCode());
        assertNotEquals(nil().cons(1).cons(2).hashCode(), nil().cons(1).cons(3).hashCode());
    }
}