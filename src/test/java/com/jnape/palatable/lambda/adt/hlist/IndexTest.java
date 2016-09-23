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
    public void getHeadAcrossHListTypes() {
        Index<String, HCons<String, ?>> stringAtHead = index();

        assertEquals("nil cons", stringAtHead.get(nil().cons("nil cons")));
        assertEquals("cons nil", stringAtHead.get(cons("cons nil", nil())));
        assertEquals("cons cons nil", stringAtHead.get(cons("cons cons nil", cons("", nil()))));
        assertEquals("singletonHList", stringAtHead.get(singletonHList("singletonHList")));
        assertEquals("2-tuple", stringAtHead.get(tuple("2-tuple", 1)));
        assertEquals("3-tuple", stringAtHead.get(tuple("3-tuple", 1, 1)));
        assertEquals("4-tuple", stringAtHead.get(tuple("4-tuple", 1, 1, 1)));
        assertEquals("5-tuple", stringAtHead.get(tuple("5-tuple", 1, 1, 1, 1)));
    }

    @Test
    public void deepGetAcrossHListTypes() {
        Index<String, HCons<Character, ? extends HCons<Boolean, ? extends HCons<Double, ? extends HCons<Integer, ? extends HCons<String, ?>>>>>> string5Deep =
                Index.<String>index()
                        .<Integer>after()
                        .<Double>after()
                        .<Boolean>after()
                        .after();

        assertEquals("plain cons", string5Deep.get(cons('c', cons(true, cons(1d, cons(2, cons("plain cons", nil())))))));
        assertEquals("consing nil", string5Deep.get(nil().cons("consing nil").cons(2).cons(1d).cons(true).cons('c')));
        assertEquals("consing cons", string5Deep.get(cons("consing cons", nil()).cons(2).cons(1d).cons(true).cons('c')));
        assertEquals("consing singletonHList", string5Deep.get(singletonHList("consing singletonHList").cons(2).cons(1d).cons(true).cons('c')));
        assertEquals("consing 2-tuple", string5Deep.get(tuple(2, "consing 2-tuple").cons(1d).cons(true).cons('c')));
        assertEquals("consing 3-tuple", string5Deep.get(tuple(1d, 2, "consing 3-tuple").cons(true).cons('c')));
        assertEquals("consing 4-tuple", string5Deep.get(tuple(true, 1d, 2, "consing 4-tuple").cons('c')));
        assertEquals("5-tuple", string5Deep.get(tuple('c', true, 1d, 2, "5-tuple")));
        assertEquals("unnecessarily deep HList", string5Deep.get(tuple(1d, 2, "unnecessarily deep HList", "deeper", "deeper still").cons(true).cons('c')));
    }

    @Test
    public void setHeadAcrossHListTypes() {
        Index<String, HCons<String, ?>> stringAtHead = index();

        assertEquals(singletonHList("nil cons"), stringAtHead.set("nil cons", nil().cons("")));
        assertEquals(tuple("tuple2", 1), stringAtHead.set("tuple2", nil().cons(1).cons("")));
        assertEquals(tuple("tuple3", 2, 1), stringAtHead.set("tuple3", nil().cons(1).cons(2).cons("")));
        assertEquals(tuple("tuple4", 3, 2, 1), stringAtHead.set("tuple4", nil().cons(1).cons(2).cons(3).cons("")));
        assertEquals(tuple("tuple5", 4, 3, 2, 1), stringAtHead.set("tuple5", nil().cons(1).cons(2).cons(3).cons(4).cons("")));
    }

    @Test
    public void deepSetAcrossHListTypes() {
        Index<String, HCons<Character, ? extends HCons<Boolean, ? extends HCons<Double, ? extends HCons<Integer, ? extends HCons<String, ?>>>>>> string5Deep =
                Index.<String>index()
                        .<Integer>after()
                        .<Double>after()
                        .<Boolean>after()
                        .after();

        assertEquals(cons('c', cons(true, cons(1d, cons(2, cons("plain cons", nil()))))),
                     string5Deep.set("plain cons", cons('c', cons(true, cons(1d, cons(2, cons("", nil())))))));

        assertEquals(nil().cons("consing nil").cons(2).cons(1d).cons(true).cons('c'),
                     string5Deep.set("consing nil", nil().cons("").cons(2).cons(1d).cons(true).cons('c')));

        assertEquals(cons("consing cons", nil()).cons(2).cons(1d).cons(true).cons('c'),
                     string5Deep.set("consing cons", cons("", nil()).cons(2).cons(1d).cons(true).cons('c')));

        assertEquals(tuple('c', true, 1d, 2, "5-tuple"), string5Deep.set("5-tuple", tuple('c', true, 1d, 2, "")));

        assertEquals(tuple(1d, 2, "deep list", "deeper", "deeper still").cons(true).cons('c'),
                     string5Deep.set("deep list", tuple(1d, 2, "", "deeper", "deeper still").cons(true).cons('c')));
    }
}