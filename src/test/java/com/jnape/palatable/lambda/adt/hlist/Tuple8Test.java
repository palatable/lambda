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
public class Tuple8Test {

    private Tuple8<Short, Byte, Float, Integer, String, Character, Boolean, Long> tuple8;

    @Before
    public void setUp() {
        tuple8 = new Tuple8<>((short) 65535, new Tuple7<>((byte) 127, new Tuple6<>(2.0f, new Tuple5<>(1, new Tuple4<>("2", new Tuple3<>('3', new Tuple2<>(false, new SingletonHList<>(5L))))))));
    }

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, MonadLaws.class, BifunctorLaws.class, TraversableLaws.class})
    public Tuple8 testSubject() {
        return tuple("one", 2, 3d, 4f, '5', (byte) 6, 7L, (short) 65535);
    }

    @Test
    public void head() {
        assertEquals((Short) (short) 65535, tuple8.head());
    }

    @Test
    public void tail() {
        assertEquals(new Tuple7<>((byte) 127, new Tuple6<>(2.0f, new Tuple5<>(1, new Tuple4<>("2", new Tuple3<>('3', new Tuple2<>(false, new SingletonHList<>(5L))))))),
                     tuple8.tail());
    }

    @Test
    public void cons() {
        assertEquals(new HCons<>(0, tuple8), tuple8.cons(0));
    }

    @Test
    public void accessors() {
        assertEquals((Short) (short) 65535, tuple8._1());
        assertEquals((Byte) (byte) 127, tuple8._2());
        assertEquals((Float) 2.0f, tuple8._3());
        assertEquals((Integer) 1, tuple8._4());
        assertEquals("2", tuple8._5());
        assertEquals((Character) '3', tuple8._6());
        assertEquals(false, tuple8._7());
        assertEquals((Long) 5L, tuple8._8());
    }

    @Test
    public void randomAccess() {
        Tuple7<String, String, String, String, String, String, String> spiedTail = spy(tuple("second", "third", "fourth", "fifth", "sixth", "seventh", "eighth"));
        Tuple8<String, String, String, String, String, String, String, String> tuple8 = new Tuple8<>("first", spiedTail);

        verify(spiedTail, times(1))._1();
        verify(spiedTail, times(1))._2();
        verify(spiedTail, times(1))._3();
        verify(spiedTail, times(1))._4();
        verify(spiedTail, times(1))._5();
        verify(spiedTail, times(1))._6();
        verify(spiedTail, times(1))._7();
        tuple8._1();
        tuple8._2();
        tuple8._3();
        tuple8._4();
        tuple8._5();
        tuple8._6();
        tuple8._7();
        tuple8._8();
        verifyNoMoreInteractions(spiedTail);
    }

    @Test
    public void fill() {
        assertEquals(tuple("foo", "foo", "foo", "foo", "foo", "foo", "foo", "foo"), Tuple8.fill("foo"));
    }

    @Test
    public void zipPrecedence() {
        Tuple8<String, Integer, Integer, Integer, Integer, Integer, Integer, Integer> a = tuple("foo", 1, 2, 3, 4, 5, 6, 7);
        Tuple8<String, Integer, Integer, Integer, Integer, Integer, Integer, Function<? super Integer, ? extends Integer>> b = tuple("bar", 2, 3, 4, 5, 6, 7, x -> x + 1);
        assertEquals(tuple("foo", 1, 2, 3, 4, 5, 6, 8), a.zip(b));
    }

    @Test
    public void flatMapPrecedence() {
        Tuple8<String, Integer, Integer, Integer, Integer, Integer, Integer, Integer> a = tuple("foo", 1, 2, 3, 4, 5, 6, 7);
        Function<Integer, Tuple8<String, Integer, Integer, Integer, Integer, Integer, Integer, Integer>> b = x -> tuple("bar", 2, 3, 4, 5, 6, 7, x + 1);
        assertEquals(tuple("foo", 1, 2, 3, 4, 5, 6, 8), a.flatMap(b));
    }
}