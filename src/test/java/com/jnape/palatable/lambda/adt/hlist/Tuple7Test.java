package com.jnape.palatable.lambda.adt.hlist;

import com.jnape.palatable.lambda.adt.hlist.HList.HCons;
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
public class Tuple7Test {

    private Tuple7<Byte, Float, Integer, String, Character, Boolean, Long> tuple7;

    @Before
    public void setUp() {
        tuple7 = new Tuple7<>((byte) 127, new Tuple6<>(2.0f, new Tuple5<>(1, new Tuple4<>("2", new Tuple3<>('3', new Tuple2<>(false, new SingletonHList<>(5L)))))));
    }

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, MonadLaws.class, BifunctorLaws.class, TraversableLaws.class})
    public Tuple7 testSubject() {
        return tuple("one", 2, 3d, 4f, '5', (byte) 6, 7L);
    }

    @Test
    public void head() {
        assertEquals((Byte) (byte) 127, tuple7.head());
    }

    @Test
    public void tail() {
        assertEquals(new Tuple6<>(2.0f, new Tuple5<>(1, new Tuple4<>("2", new Tuple3<>('3', new Tuple2<>(false, new SingletonHList<>(5L)))))),
                     tuple7.tail());
    }

    @Test
    public void cons() {
        assertEquals(new HCons<>(0, tuple7), tuple7.cons(0));
    }

    @Test
    public void accessors() {
        assertEquals((Byte) (byte) 127, tuple7._1());
        assertEquals((Float) 2.0f, tuple7._2());
        assertEquals((Integer) 1, tuple7._3());
        assertEquals("2", tuple7._4());
        assertEquals((Character) '3', tuple7._5());
        assertEquals(false, tuple7._6());
        assertEquals((Long) 5L, tuple7._7());
    }

    @Test
    public void randomAccess() {
        Tuple6<String, String, String, String, String, String> spiedTail = spy(tuple("second", "third", "fourth", "fifth", "sixth", "seventh"));
        Tuple7<String, String, String, String, String, String, String> tuple7 = new Tuple7<>("first", spiedTail);

        verify(spiedTail, times(1))._1();
        verify(spiedTail, times(1))._2();
        verify(spiedTail, times(1))._3();
        verify(spiedTail, times(1))._4();
        verify(spiedTail, times(1))._5();
        verify(spiedTail, times(1))._6();
        tuple7._1();
        tuple7._2();
        tuple7._3();
        tuple7._4();
        tuple7._5();
        tuple7._6();
        tuple7._7();
        verifyNoMoreInteractions(spiedTail);
    }

    @Test
    public void fill() {
        assertEquals(tuple("foo", "foo", "foo", "foo", "foo", "foo", "foo"), Tuple7.fill("foo"));
    }

    @Test
    public void zipPrecedence() {
        Tuple7<String, Integer, Integer, Integer, Integer, Integer, Integer> a = tuple("foo", 1, 2, 3, 4, 5, 6);
        Tuple7<String, Integer, Integer, Integer, Integer, Integer, Function<? super Integer, ? extends Integer>> b = tuple("bar", 2, 3, 4, 5, 6, x -> x + 1);
        assertEquals(tuple("foo", 1, 2, 3, 4, 5, 7), a.zip(b));
    }

    @Test
    public void flatMapPrecedence() {
        Tuple7<String, Integer, Integer, Integer, Integer, Integer, Integer> a = tuple("foo", 1, 2, 3, 4, 5, 6);
        Function<Integer, Tuple7<String, Integer, Integer, Integer, Integer, Integer, Integer>> b = x -> tuple("bar", 2, 3, 4, 5, 6, x + 1);
        assertEquals(tuple("foo", 1, 2, 3, 4, 5, 7), a.flatMap(b));
    }
}