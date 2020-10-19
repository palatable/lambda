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
import testsupport.traits.MonadRecLaws;
import testsupport.traits.TraversableLaws;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.adt.hlist.Tuple7.pureTuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Repeat.repeat;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
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

    @TestTraits({
            FunctorLaws.class,
            ApplicativeLaws.class,
            MonadLaws.class,
            MonadRecLaws.class,
            BifunctorLaws.class,
            TraversableLaws.class})
    public Tuple7<?, ?, ?, ?, ?, ?, ?> testSubject() {
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
    public void snoc() {
        assertEquals(tuple("b", 7L, "c", 11, "d", 13, "e", 'f'), tuple("b", 7L, "c", 11, "d", 13, "e").snoc('f'));
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
        Tuple6<String, String, String, String, String, String>         spiedTail = spy(tuple("second", "third", "fourth", "fifth", "sixth", "seventh"));
        Tuple7<String, String, String, String, String, String, String> tuple7    = new Tuple7<>("first", spiedTail);

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
    public void into() {
        Tuple7<String, Integer, Double, Boolean, Float, Short, Byte> tuple = tuple("foo", 1, 2.0d, false, 3f, (short) 4, (byte) 5);
        assertEquals("foo12.0false3.045", tuple.into((s, i, d, b, f, sh, by) -> s + i + d + b + f + sh + by));
    }

    @Test
    public void zipPrecedence() {
        Tuple7<String, Integer, Integer, Integer, Integer, Integer, Integer> a =
                tuple("foo", 1, 2, 3, 4, 5, 6);
        Tuple7<String, Integer, Integer, Integer, Integer, Integer, Fn1<? super Integer, ? extends Integer>> b =
                tuple("bar", 2, 3, 4, 5, 6, x -> x + 1);
        assertEquals(tuple("foo", 1, 2, 3, 4, 5, 7), a.zip(b));
    }

    @Test
    public void flatMapPrecedence() {
        Tuple7<String, Integer, Integer, Integer, Integer, Integer, Integer>               a = tuple("foo", 1, 2, 3, 4, 5, 6);
        Fn1<Integer, Tuple7<String, Integer, Integer, Integer, Integer, Integer, Integer>> b = x -> tuple("bar", 2, 3, 4, 5, 6, x + 1);
        assertEquals(tuple("foo", 1, 2, 3, 4, 5, 7), a.flatMap(b));
    }

    @Test
    public void fromIterable() {
        assertEquals(nothing(), Tuple7.fromIterable(emptyList()));
        assertEquals(nothing(), Tuple7.fromIterable(singletonList(1)));
        assertEquals(just(tuple(1, 1, 1, 1, 1, 1, 1)), Tuple7.fromIterable(repeat(1)));
    }

    @Test
    public void staticPure() {
        Tuple7<Byte, Short, Integer, Long, Float, Double, Boolean> tuple =
                pureTuple((byte) 1, (short) 2, 3, 4L, 5F, 6D).apply(true);
        assertEquals(tuple((byte) 1, (short) 2, 3, 4L, 5F, 6D, true), tuple);
    }

    @Test
    public void init() {
        assertEquals(tuple(1, 2, 3, 4, 5, 6),
                     tuple(1, 2, 3, 4, 5, 6, 7).init());
    }
}