package com.jnape.palatable.lambda.adt.hlist;

import com.jnape.palatable.lambda.adt.hlist.HList.HCons;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.hlist.HList.cons;
import static com.jnape.palatable.lambda.adt.hlist.HList.nil;
import static com.jnape.palatable.lambda.adt.hlist.HList.singletonHList;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.adt.hlist.Index.index;
import static org.junit.Assert.assertEquals;

public class IndexTest {

    @Test
    public void headAccess() {
        Index<String, HCons<String, ?>> stringAtHead = index();

        assertEquals("plain cons", stringAtHead.get(nil().cons("plain cons")));
        assertEquals("plain cons", stringAtHead.get(cons("plain cons", nil())));
        assertEquals("plain cons", stringAtHead.get(cons("plain cons", cons("foo", nil()))));
        assertEquals("plain cons", stringAtHead.get(cons("plain cons", cons(1, nil()))));

        assertEquals("singletonHList", stringAtHead.get(singletonHList("singletonHList")));
        assertEquals("singletonHList with cons", stringAtHead.get(singletonHList(1).cons("singletonHList with cons")));

        assertEquals("2-tuple", stringAtHead.get(tuple("2-tuple", 1)));
        assertEquals("2-tuple with cons", stringAtHead.get(tuple(1, 1).cons("2-tuple with cons")));

        assertEquals("3-tuple", stringAtHead.get(tuple("3-tuple", 1, 1)));
        assertEquals("3-tuple with cons", stringAtHead.get(tuple(1, 1, 1).cons("3-tuple with cons")));

        assertEquals("4-tuple", stringAtHead.get(tuple("4-tuple", 1, 1, 1)));
        assertEquals("4-tuple with cons", stringAtHead.get(tuple(1, 1, 1, 1).cons("4-tuple with cons")));

        assertEquals("5-tuple", stringAtHead.get(tuple("5-tuple", 1, 1, 1, 1)));
        assertEquals("5-tuple with cons", stringAtHead.get(tuple(1, 1, 1, 1, 1).cons("5-tuple with cons")));
    }

    @Test
    public void deepAccess() {
        Index<String, HCons<Character, ? extends HCons<Boolean, ? extends HCons<Double, ? extends HCons<Integer, ? extends HCons<String, ?>>>>>> deepString =
                Index.<String>index()
                        .<Integer>after()
                        .<Double>after()
                        .<Boolean>after()
                        .after();

        assertEquals("plain cons", deepString.get(cons('c', cons(true, cons(1d, cons(2, cons("plain cons", nil())))))));
        assertEquals("consing nil", deepString.get(nil().cons("consing nil").cons(2).cons(1d).cons(true).cons('c')));
        assertEquals("consing cons", deepString.get(cons("consing cons", nil()).cons(2).cons(1d).cons(true).cons('c')));
        assertEquals("consing singletonHList", deepString.get(singletonHList("consing singletonHList").cons(2).cons(1d).cons(true).cons('c')));
        assertEquals("consing 2-tuple", deepString.get(tuple(2, "consing 2-tuple").cons(1d).cons(true).cons('c')));
        assertEquals("consing 3-tuple", deepString.get(tuple(1d, 2, "consing 3-tuple").cons(true).cons('c')));
        assertEquals("consing 4-tuple", deepString.get(tuple(true, 1d, 2, "consing 4-tuple").cons('c')));
        assertEquals("5-tuple", deepString.get(tuple('c', true, 1d, 2, "5-tuple")));
        assertEquals("unnecessarily deep HList", deepString.get(tuple(1d, 2, "unnecessarily deep HList", "deeper", "deeper still").cons(true).cons('c')));
    }
}