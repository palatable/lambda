package com.jnape.palatable.lambda.adt.hlist;

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

import java.time.Duration;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.adt.hlist.Tuple3.pureTuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Repeat.repeat;
import static java.time.Duration.ofSeconds;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(Traits.class)
public class Tuple3Test {

    private Tuple3<Integer, String, Character> tuple3;

    @Before
    public void setUp() {
        tuple3 = new Tuple3<>(1, new Tuple2<>("2", new SingletonHList<>('3')));
    }

    @TestTraits({
            FunctorLaws.class,
            ApplicativeLaws.class,
            MonadLaws.class,
            MonadRecLaws.class,
            BifunctorLaws.class,
            TraversableLaws.class})
    public Tuple3<?, ?, ?> testSubject() {
        return tuple("one", 2, 3d);
    }

    @Test
    public void head() {
        assertEquals((Integer) 1, tuple3.head());
    }

    @Test
    public void tail() {
        assertEquals(new Tuple2<>("2", new SingletonHList<>('3')), tuple3.tail());
    }

    @Test
    public void cons() {
        assertEquals(new Tuple4<>(0, tuple3), tuple3.cons(0));
    }

    @Test
    public void accessors() {
        assertEquals((Integer) 1, tuple3._1());
        assertEquals("2", tuple3._2());
        assertEquals((Character) '3', tuple3._3());
    }

    @Test
    public void randomAccess() {
        Tuple2<String, String>         spiedTail = spy(tuple("second", "third"));
        Tuple3<String, String, String> tuple3    = new Tuple3<>("first", spiedTail);

        verify(spiedTail, times(1))._1();
        verify(spiedTail, times(1))._2();
        tuple3._1();
        tuple3._2();
        tuple3._3();
        verifyNoMoreInteractions(spiedTail);
    }

    @Test
    public void into() {
        Tuple3<String, Integer, Double> tuple = tuple("foo", 1, 2.0d);
        assertEquals("foo12.0", tuple.into((s, i, d) -> s + i + d));
    }

    @Test
    public void fill() {
        assertEquals(tuple("foo", "foo", "foo"), Tuple3.fill("foo"));
    }

    @Test
    public void zipPrecedence() {
        Tuple3<String, Integer, Integer>                                 a = tuple("foo", 1, 2);
        Tuple3<String, Integer, Fn1<? super Integer, ? extends Integer>> b = tuple("bar", 2, x -> x + 1);
        assertEquals(tuple("foo", 1, 3), a.zip(b));
    }

    @Test
    public void flatMapPrecedence() {
        Tuple3<String, Integer, Integer>               a = tuple("foo", 1, 2);
        Fn1<Integer, Tuple3<String, Integer, Integer>> b = x -> tuple("bar", 2, x + 1);
        assertEquals(tuple("foo", 1, 3), a.flatMap(b));
    }

    @Test
    public void fromIterable() {
        assertEquals(nothing(), Tuple3.fromIterable(emptyList()));
        assertEquals(nothing(), Tuple3.fromIterable(singletonList(1)));
        assertEquals(just(tuple(1, 1, 1)), Tuple3.fromIterable(repeat(1)));
    }

    @Test
    public void staticPure() {
        Tuple3<Integer, String, Character> tuple = pureTuple(1, "2").apply('3');
        assertEquals(tuple(1, "2", '3'), tuple);
    }

    @Test
    public void snoc() {
        Tuple4<String, Long, Integer, Duration> tuple = tuple("qux", Long.MIN_VALUE, 7).snoc(ofSeconds(13));
        assertEquals(tuple("qux", Long.MIN_VALUE, 7, ofSeconds(13)), tuple);
    }
}
