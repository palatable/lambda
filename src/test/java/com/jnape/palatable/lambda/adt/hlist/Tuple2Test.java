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
import testsupport.traits.MonadWriterLaws;
import testsupport.traits.TraversableLaws;

import java.util.HashMap;
import java.util.Map;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.hlist.HList.singletonHList;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.adt.hlist.Tuple2.pureTuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Repeat.repeat;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(Traits.class)
public class Tuple2Test {

    private Tuple2<Integer, Integer> tuple2;

    @Before
    public void setUp() {
        tuple2 = new Tuple2<>(1, new SingletonHList<>(2));
    }

    @TestTraits({
            FunctorLaws.class,
            ApplicativeLaws.class,
            MonadLaws.class,
            MonadRecLaws.class,
            MonadWriterLaws.class,
            BifunctorLaws.class,
            TraversableLaws.class})
    public Tuple2<?, ?> testSubject() {
        return tuple("one", 2);
    }

    @Test
    public void head() {
        assertEquals((Integer) 1, tuple2.head());
    }

    @Test
    public void tail() {
        assertEquals(new SingletonHList<>(2), tuple2.tail());
    }

    @Test
    public void init() {
        assertEquals(new SingletonHList<>(1), tuple2.init());
    }

    @Test
    public void cons() {
        assertEquals(new Tuple3<>(0, tuple2), tuple2.cons(0));
    }

    @Test
    public void snoc() {
        assertEquals(tuple(Long.MAX_VALUE, 123, "hi"), tuple(Long.MAX_VALUE, 123).snoc("hi"));
    }

    @Test
    public void accessors() {
        assertEquals((Integer) 1, tuple2._1());
        assertEquals((Integer) 2, tuple2._2());
    }

    @Test
    public void randomAccess() {
        SingletonHList<String> spiedTail = spy(singletonHList("second"));
        Tuple2<String, String> tuple2    = new Tuple2<>("first", spiedTail);

        verify(spiedTail, only()).head();
        tuple2._1();
        tuple2._2();
        verifyNoMoreInteractions(spiedTail);
    }

    @Test
    public void into() {
        Tuple2<String, Integer> tuple = tuple("foo", 1);
        assertEquals("foo1", tuple.into((s, i) -> s + i));
    }

    @Test
    public void fill() {
        assertEquals(tuple("foo", "foo"), Tuple2.fill("foo"));
    }

    @Test
    public void mapEntryProperties() {
        assertEquals((Integer) 1, tuple2.getKey());
        assertEquals((Integer) 2, tuple2.getValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void setValueIsNotSupported() {
        tuple2.setValue(3);
    }

    @Test
    @SuppressWarnings("serial")
    public void staticFactoryMethodFromMapEntry() {
        Map.Entry<String, Integer> stringIntEntry = new HashMap<String, Integer>() {{
            put("string", 1);
        }}.entrySet().iterator().next();

        assertEquals(tuple("string", 1), Tuple2.fromEntry(stringIntEntry));
    }

    @Test
    public void zipPrecedence() {
        Tuple2<String, Integer>                                 a = tuple("foo", 1);
        Tuple2<String, Fn1<? super Integer, ? extends Integer>> b = tuple("bar", x -> x + 1);
        assertEquals(tuple("foo", 2), a.zip(b));
    }

    @Test
    public void flatMapPrecedence() {
        Tuple2<String, Integer>               a = tuple("foo", 1);
        Fn1<Integer, Tuple2<String, Integer>> b = x -> tuple("bar", x + 1);
        assertEquals(tuple("foo", 2), a.flatMap(b));
    }

    @Test
    public void fromIterable() {
        assertEquals(nothing(), Tuple2.fromIterable(emptyList()));
        assertEquals(nothing(), Tuple2.fromIterable(singletonList(1)));
        assertEquals(just(tuple(1, 1)), Tuple2.fromIterable(repeat(1)));
    }

    @Test
    public void staticPure() {
        Tuple2<Integer, String> tuple = pureTuple(1).apply("two");
        assertEquals(tuple(1, "two"), tuple);
    }
}