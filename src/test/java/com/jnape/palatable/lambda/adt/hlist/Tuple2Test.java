package com.jnape.palatable.lambda.adt.hlist;

import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.FunctorLaws;

import java.util.HashMap;
import java.util.Map;

import static com.jnape.palatable.lambda.adt.hlist.HList.singletonHList;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(Traits.class)
public class Tuple2Test {

    private Tuple2<Integer, Integer> tuple2;

    @Before
    public void setUp() throws Exception {
        tuple2 = new Tuple2<>(1, new SingletonHList<>(2));
    }

    @TestTraits({FunctorLaws.class})
    public Tuple2 testSubject() {
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
    public void cons() {
        assertEquals(new Tuple3<>(0, tuple2), tuple2.cons(0));
    }

    @Test
    public void accessors() {
        assertEquals((Integer) 1, tuple2._1());
        assertEquals((Integer) 2, tuple2._2());
    }

    @Test
    public void randomAccess() {
        SingletonHList<String> spiedTail = spy(singletonHList("second"));
        Tuple2<String, String> tuple2 = new Tuple2<>("first", spiedTail);

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
    public void bifunctorProperties() {
        assertEquals(new Tuple2<>("1", new SingletonHList<>("2")), tuple2.biMap(Object::toString, Object::toString));
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
    public void staticFactoryMethodFromMapEntry() {
        Map.Entry<String, Integer> stringIntEntry = new HashMap<String, Integer>() {{
            put("string", 1);
        }}.entrySet().iterator().next();

        assertEquals(tuple("string", 1), Tuple2.fromEntry(stringIntEntry));
    }
}