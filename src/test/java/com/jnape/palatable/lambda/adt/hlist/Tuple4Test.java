package com.jnape.palatable.lambda.adt.hlist;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.*;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.adt.hlist.Tuple4.pureTuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Repeat.repeat;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(Traits.class)
public class Tuple4Test {

    private Tuple4<Integer, String, Character, Boolean> tuple4;

    @Before
    public void setUp() {
        tuple4 = new Tuple4<>(1, new Tuple3<>("2", new Tuple2<>('3', new SingletonHList<>(false))));
    }

    @TestTraits({
            FunctorLaws.class,
            ApplicativeLaws.class,
            MonadLaws.class,
            MonadRecLaws.class,
            BifunctorLaws.class,
            TraversableLaws.class,
            ComonadLaws.class})
    public Tuple4<?, ?, ?, ?> testSubject() {
        return tuple("one", 2, 3d, 4f);
    }

    @Test
    public void head() {
        assertEquals((Integer) 1, tuple4.head());
    }

    @Test
    public void tail() {
        assertEquals(new Tuple3<>("2", new Tuple2<>('3', new SingletonHList<>(false))), tuple4.tail());
    }

    @Test
    public void cons() {
        assertEquals(new Tuple5<>(0, tuple4), tuple4.cons(0));
    }

    @Test
    public void snoc() {
        assertEquals(tuple("qux", 7, "foo", 13L, 17), tuple("qux", 7, "foo", 13L).snoc(17));
    }

    @Test
    public void accessors() {
        assertEquals((Integer) 1, tuple4._1());
        assertEquals("2", tuple4._2());
        assertEquals((Character) '3', tuple4._3());
        assertEquals(false, tuple4._4());
    }

    @Test
    public void randomAccess() {
        Tuple3<String, String, String>         spiedTail = spy(tuple("second", "third", "fourth"));
        Tuple4<String, String, String, String> tuple4    = new Tuple4<>("first", spiedTail);

        verify(spiedTail, times(1))._1();
        verify(spiedTail, times(1))._2();
        verify(spiedTail, times(1))._3();
        tuple4._1();
        tuple4._2();
        tuple4._3();
        tuple4._4();
        verifyNoMoreInteractions(spiedTail);
    }

    @Test
    public void into() {
        Tuple4<String, Integer, Double, Boolean> tuple = tuple("foo", 1, 2.0d, false);
        assertEquals("foo12.0false", tuple.into((s, i, d, b) -> s + i + d + b));
    }

    @Test
    public void fill() {
        assertEquals(tuple("foo", "foo", "foo", "foo"), Tuple4.fill("foo"));
    }

    @Test
    public void zipPrecedence() {
        Tuple4<String, Integer, Integer, Integer>                                 a = tuple("foo", 1, 2, 3);
        Tuple4<String, Integer, Integer, Fn1<? super Integer, ? extends Integer>> b = tuple("foo", 1, 2, x -> x + 1);
        assertEquals(tuple("foo", 1, 2, 4), a.zip(b));
    }

    @Test
    public void flatMapPrecedence() {
        Tuple4<String, Integer, Integer, Integer>               a = tuple("foo", 1, 2, 3);
        Fn1<Integer, Tuple4<String, Integer, Integer, Integer>> b = x -> tuple("bar", 2, 3, x + 1);
        assertEquals(tuple("foo", 1, 2, 4), a.flatMap(b));
    }

    @Test
    public void fromIterable() {
        assertEquals(nothing(), Tuple4.fromIterable(emptyList()));
        assertEquals(nothing(), Tuple4.fromIterable(singletonList(1)));
        assertEquals(just(tuple(1, 1, 1, 1)), Tuple4.fromIterable(repeat(1)));
    }

    @Test
    public void staticPure() {
        Tuple4<Integer, String, Character, Boolean> tuple = pureTuple(1, "2", '3').apply(true);
        assertEquals(tuple(1, "2", '3', true), tuple);
    }
}