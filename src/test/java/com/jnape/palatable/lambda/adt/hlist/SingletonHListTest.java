package com.jnape.palatable.lambda.adt.hlist;

import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.FunctorLaws;

import static com.jnape.palatable.lambda.adt.hlist.HList.nil;
import static com.jnape.palatable.lambda.adt.hlist.HList.singletonHList;
import static org.junit.Assert.assertEquals;

@RunWith(Traits.class)
public class SingletonHListTest {

    private SingletonHList<Integer> singletonHList;

    @Before
    public void setUp() {
        singletonHList = new SingletonHList<>(1);
    }

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class})
    public SingletonHList testSubject() {
        return singletonHList("one");
    }

    @Test
    public void head() {
        assertEquals((Integer) 1, singletonHList.head());
    }

    @Test
    public void tail() {
        assertEquals(nil(), singletonHList.tail());
    }

    @Test
    public void cons() {
        assertEquals(new Tuple2<>("0", singletonHList), singletonHList.cons("0"));
    }
}