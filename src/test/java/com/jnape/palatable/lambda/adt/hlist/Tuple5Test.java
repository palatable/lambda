package com.jnape.palatable.lambda.adt.hlist;

import com.jnape.palatable.lambda.adt.hlist.HList.HCons;
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
import static com.jnape.palatable.lambda.adt.hlist.Tuple5.pureTuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Repeat.repeat;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(Traits.class)
public class Tuple5Test {

    private Tuple5<Integer, String, Character, Boolean, Long> tuple5;

    @Before
    public void setUp() {
        tuple5 = new Tuple5<>(1, new Tuple4<>("2", new Tuple3<>('3', new Tuple2<>(false, new SingletonHList<>(5L)))));
    }

    @TestTraits({
            FunctorLaws.class,
            ApplicativeLaws.class,
            MonadLaws.class,
            MonadRecLaws.class,
            BifunctorLaws.class,
            TraversableLaws.class})
    public Tuple5<?, ?, ?, ?, ?> testSubject() {
        return tuple("one", 2, 3d, 4f, '5');
    }

    @Test
    public void head() {
        assertEquals((Integer) 1, tuple5.head());
    }

    @Test
    public void tail() {
        assertEquals(new Tuple4<>("2", new Tuple3<>('3', new Tuple2<>(false, new SingletonHList<>(5L)))), tuple5.tail());
    }

    @Test
    public void cons() {
        assertEquals(new HCons<>(0, tuple5), tuple5.cons(0));
    }

    @Test
    public void snoc() {
        assertEquals(tuple("a", 5, "b", 7, "c", 11), tuple("a", 5, "b", 7, "c").snoc(11));
    }

    @Test
    public void accessors() {
        assertEquals((Integer) 1, tuple5._1());
        assertEquals("2", tuple5._2());
        assertEquals((Character) '3', tuple5._3());
        assertEquals(false, tuple5._4());
        assertEquals((Long) 5L, tuple5._5());
    }

    @Test
    public void randomAccess() {
        Tuple4<String, String, String, String>         spiedTail = spy(tuple("second", "third", "fourth", "fifth"));
        Tuple5<String, String, String, String, String> tuple5    = new Tuple5<>("first", spiedTail);

        verify(spiedTail, times(1))._1();
        verify(spiedTail, times(1))._2();
        verify(spiedTail, times(1))._3();
        verify(spiedTail, times(1))._4();
        tuple5._1();
        tuple5._2();
        tuple5._3();
        tuple5._4();
        tuple5._5();
        verifyNoMoreInteractions(spiedTail);
    }

    @Test
    public void into() {
        Tuple5<String, Integer, Double, Boolean, Float> tuple = tuple("foo", 1, 2.0d, false, 3f);
        assertEquals("foo12.0false3.0", tuple.into((s, i, d, b, f) -> s + i + d + b + f));
    }

    @Test
    public void fill() {
        assertEquals(tuple("foo", "foo", "foo", "foo", "foo"), Tuple5.fill("foo"));
    }

    @Test
    public void zipPrecedence() {
        Tuple5<String, Integer, Integer, Integer, Integer> a =
                tuple("foo", 1, 2, 3, 4);
        Tuple5<String, Integer, Integer, Integer, Fn1<? super Integer, ? extends Integer>> b =
                tuple("bar", 2, 3, 4, x -> x + 1);
        assertEquals(tuple("foo", 1, 2, 3, 5), a.zip(b));
    }

    @Test
    public void flatMapPrecedence() {
        Tuple5<String, Integer, Integer, Integer, Integer>               a = tuple("foo", 1, 2, 3, 4);
        Fn1<Integer, Tuple5<String, Integer, Integer, Integer, Integer>> b = x -> tuple("bar", 2, 3, 4, x + 1);
        assertEquals(tuple("foo", 1, 2, 3, 5), a.flatMap(b));
    }

    @Test
    public void fromIterable() {
        assertEquals(nothing(), Tuple5.fromIterable(emptyList()));
        assertEquals(nothing(), Tuple5.fromIterable(singletonList(1)));
        assertEquals(just(tuple(1, 1, 1, 1, 1)), Tuple5.fromIterable(repeat(1)));
    }

    @Test
    public void staticPure() {
        Tuple5<Integer, String, Character, Boolean, Float> tuple = pureTuple(1, "2", '3', true).apply(5f);
        assertEquals(tuple(1, "2", '3', true, 5f), tuple);
    }

    @Test
    public void init() {
        assertEquals(tuple(1, 2, 3, 4),
                     tuple(1, 2, 3, 4, 5).init());
    }
}