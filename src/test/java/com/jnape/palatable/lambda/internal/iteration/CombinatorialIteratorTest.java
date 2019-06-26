package com.jnape.palatable.lambda.internal.iteration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Iterator;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static testsupport.Mocking.mockIteratorToHaveValues;

@RunWith(MockitoJUnitRunner.class)
public class CombinatorialIteratorTest {

    @Mock private Iterator<Object> as;
    @Mock private Iterator<Object> bs;

    private CombinatorialIterator<Object, Object> combinatorialIterator;

    @Before
    public void setUp() {
        combinatorialIterator = new CombinatorialIterator<>(as, bs);
    }

    @Test
    public void hasNextIfMoreAsAndMoreBs() {
        when(as.hasNext()).thenReturn(true);
        when(bs.hasNext()).thenReturn(true);

        assertThat(combinatorialIterator.hasNext(), is(true));
    }

    @Test
    public void doesNotHaveNextIfMoreAsButNoBs() {
        when(as.hasNext()).thenReturn(true);
        when(bs.hasNext()).thenReturn(false);

        assertThat(combinatorialIterator.hasNext(), is(false));
    }

    @Test
    public void doesNotHaveNextIfNoAsButMoreBs() {
        when(as.hasNext()).thenReturn(false);

        assertThat(combinatorialIterator.hasNext(), is(false));
    }

    @Test
    public void computesCombinationsInOrder() {
        mockIteratorToHaveValues(as, "a1", "a2");
        mockIteratorToHaveValues(bs, "b1", "b2");

        assertThat(combinatorialIterator.next(), is(tuple("a1", "b1")));
        assertThat(combinatorialIterator.next(), is(tuple("a1", "b2")));
        assertThat(combinatorialIterator.next(), is(tuple("a2", "b1")));
        assertThat(combinatorialIterator.next(), is(tuple("a2", "b2")));
    }
}
