package com.jnape.palatable.lambda.adt.hlist;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.hlist.HList.cons;
import static com.jnape.palatable.lambda.adt.hlist.HList.nil;
import static com.jnape.palatable.lambda.adt.hlist.HList.singletonHList;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

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
    public void autoPromotion() {
        assertThat(cons(1, nil()), instanceOf(SingletonHList.class));
        assertThat(cons(1, singletonHList(1)), instanceOf(Tuple2.class));
        assertThat(cons(1, tuple(1, 1)), instanceOf(Tuple3.class));
        assertThat(cons(1, tuple(1, 1, 1)), instanceOf(Tuple4.class));
        assertThat(cons(1, tuple(1, 1, 1, 1)), instanceOf(Tuple5.class));
        assertThat(cons(1, tuple(1, 1, 1, 1, 1)), instanceOf(Tuple6.class));
        assertThat(cons(1, tuple(1, 1, 1, 1, 1, 1)), instanceOf(Tuple7.class));
        assertThat(cons(1, tuple(1, 1, 1, 1, 1, 1, 1)), instanceOf(Tuple8.class));
    }

    @Test
    public void nilReusesInstance() {
        assertSame(nil(), nil());
    }

    @Test
    public void equality() {
        assertEquals(nil(), nil());
        assertEquals(cons(1, nil()), cons(1, nil()));

        assertNotEquals(cons(1, nil()), nil());
        assertNotEquals(nil(), cons(1, nil()));

        assertNotEquals(cons(1, cons(2, nil())), cons(1, nil()));
        assertNotEquals(cons(1, nil()), cons(1, cons(2, nil())));
    }

    @Test
    public void hashCodeUsesDecentDistribution() {
        assertEquals(nil().hashCode(), nil().hashCode());
        assertEquals(nil().cons(1).hashCode(), nil().cons(1).hashCode());

        assertNotEquals(nil().cons(1).hashCode(), nil().cons(2).hashCode());
        assertNotEquals(nil().cons(1).cons(2).hashCode(), nil().cons(1).cons(3).hashCode());
    }
}