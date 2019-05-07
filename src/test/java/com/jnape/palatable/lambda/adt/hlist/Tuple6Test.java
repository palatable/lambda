package com.jnape.palatable.lambda.adt.hlist;

import com.jnape.palatable.lambda.adt.hlist.HList.HCons;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.BifunctorLaws;
import testsupport.traits.FunctorLaws;
import testsupport.traits.MonadLaws;
import testsupport.traits.TraversableLaws;

import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(Traits.class)
public class Tuple6Test {

    private Tuple6<Float, Integer, String, Character, Boolean, Long> tuple6;

    @Before
    public void setUp() {
        tuple6 = new Tuple6<>(2.0f, new Tuple5<>(1, new Tuple4<>("2", new Tuple3<>('3', new Tuple2<>(false, new SingletonHList<>(5L))))));
    }

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, MonadLaws.class, BifunctorLaws.class, TraversableLaws.class})
    public Tuple6<?, ?, ?, ?, ?, ?> testSubject() {
        return tuple("one", 2, 3d, 4f, '5', (byte) 6);
    }

    @Test
    public void head() {
        assertEquals((Float) 2.0F, tuple6.head());
    }

    @Test
    public void tail() {
        assertEquals(new Tuple5<>(1, new Tuple4<>("2", new Tuple3<>('3', new Tuple2<>(false, new SingletonHList<>(5L))))),
                     tuple6.tail());
    }

    @Test
    public void cons() {
        assertEquals(new HCons<>(0, tuple6), tuple6.cons(0));
    }

    @Test
    public void accessors() {
        assertEquals((Float) 2.0f, tuple6._1());
        assertEquals((Integer) 1, tuple6._2());
        assertEquals("2", tuple6._3());
        assertEquals((Character) '3', tuple6._4());
        assertEquals(false, tuple6._5());
        assertEquals((Long) 5L, tuple6._6());
    }

    @Test
    public void randomAccess() {
        Tuple5<String, String, String, String, String>         spiedTail = spy(tuple("second", "third", "fourth", "fifth", "sixth"));
        Tuple6<String, String, String, String, String, String> tuple6    = new Tuple6<>("first", spiedTail);

        verify(spiedTail, times(1))._1();
        verify(spiedTail, times(1))._2();
        verify(spiedTail, times(1))._3();
        verify(spiedTail, times(1))._4();
        verify(spiedTail, times(1))._5();
        tuple6._1();
        tuple6._2();
        tuple6._3();
        tuple6._4();
        tuple6._5();
        tuple6._6();
        verifyNoMoreInteractions(spiedTail);
    }

    @Test
    public void into() {
        Tuple6<String, Integer, Double, Boolean, Float, Short> tuple = tuple("foo", 1, 2.0d, false, 3f, (short) 4);
        assertEquals("foo12.0false3.04", tuple.into((s, i, d, b, f, sh) -> s + i + d + b + f + sh));
    }

    @Test
    public void fill() {
        assertEquals(tuple("foo", "foo", "foo", "foo", "foo", "foo"), Tuple6.fill("foo"));
    }

    @Test
    public void zipPrecedence() {
        Tuple6<String, Integer, Integer, Integer, Integer, Integer> a =
                tuple("foo", 1, 2, 3, 4, 5);
        Tuple6<String, Integer, Integer, Integer, Integer, Fn1<? super Integer, ? extends Integer>> b =
                tuple("bar", 2, 3, 4, 5, x -> x + 1);
        assertEquals(tuple("bar", 2, 3, 4, 5, 6), a.zip(b));
    }

    @Test
    public void flatMapPrecedence() {
        Tuple6<String, Integer, Integer, Integer, Integer, Integer>               a = tuple("foo", 1, 2, 3, 4, 5);
        Fn1<Integer, Tuple6<String, Integer, Integer, Integer, Integer, Integer>> b = x -> tuple("bar", 2, 3, 4, 5, x + 1);
        assertEquals(tuple("foo", 1, 2, 3, 4, 6), a.flatMap(b));
    }
}