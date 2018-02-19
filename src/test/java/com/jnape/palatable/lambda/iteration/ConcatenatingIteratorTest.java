package com.jnape.palatable.lambda.iteration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Iterator;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Last.last;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Size.size;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Iterate.iterate;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Take.take;
import static com.jnape.palatable.lambda.functions.builtin.fn3.FoldRight.foldRight;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static testsupport.Mocking.mockIteratorToHaveValues;

@RunWith(MockitoJUnitRunner.class)
public class ConcatenatingIteratorTest {

    @Mock private Iterator<Integer> xs;
    @Mock private Iterator<Integer> ys;

    private ConcatenatingIterator<Integer> concatenatingIterator;

    @Before
    public void setUp() throws Exception {
        concatenatingIterator = new ConcatenatingIterator<>(() -> xs, () -> ys);
    }

    @Test
    public void hasNextIfMoreXsAndMoreYs() {
        when(xs.hasNext()).thenReturn(true);
        when(ys.hasNext()).thenReturn(true);

        assertTrue(concatenatingIterator.hasNext());
    }

    @Test
    public void hasNextIfJustMoreXs() {
        when(xs.hasNext()).thenReturn(true);
        when(ys.hasNext()).thenReturn(false);

        assertTrue(concatenatingIterator.hasNext());
    }

    @Test
    public void hasNextIfJustMoreYs() {
        when(xs.hasNext()).thenReturn(false);
        when(ys.hasNext()).thenReturn(true);

        assertTrue(concatenatingIterator.hasNext());
    }

    @Test
    public void doesNotHaveNextIfNeitherMoreXsNorMoreYs() {
        when(xs.hasNext()).thenReturn(false);
        when(ys.hasNext()).thenReturn(false);

        assertFalse(concatenatingIterator.hasNext());
    }

    @Test
    public void nextPullsFromXsFirstThenFromYs() {
        mockIteratorToHaveValues(xs, 1, 2);
        mockIteratorToHaveValues(ys, 3);

        assertThat(concatenatingIterator.hasNext(), is(true));
        assertThat(concatenatingIterator.next(), is(1));
        assertThat(concatenatingIterator.hasNext(), is(true));
        assertThat(concatenatingIterator.next(), is(2));
        assertThat(concatenatingIterator.hasNext(), is(true));
        assertThat(concatenatingIterator.next(), is(3));
        assertThat(concatenatingIterator.hasNext(), is(false));
    }

    @Test
    public void stackSafety() {
        Integer stackBlowingNumber = 50000;
        Iterable<Iterable<Integer>> xss = map(Collections::singleton, take(stackBlowingNumber, iterate(x -> x + 1, 1)));
        Iterable<Integer> deeplyNestedConcat = foldRight((xs, ys) -> () -> new ConcatenatingIterator<>(xs, ys),
                                                         (Iterable<Integer>) Collections.<Integer>emptySet(),
                                                         xss);

        Iterable<Integer> ints = () -> new ConcatenatingIterator<>(deeplyNestedConcat, deeplyNestedConcat);

        assertEquals(just(stackBlowingNumber), last(ints));
        assertEquals((long) (stackBlowingNumber * 2), size(ints).longValue());
    }
}