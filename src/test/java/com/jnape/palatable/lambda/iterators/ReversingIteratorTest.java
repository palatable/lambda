package com.jnape.palatable.lambda.iterators;

import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Iterator;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static testsupport.Mocking.mockIteratorToHaveValues;

@RunWith(MockitoJUnitRunner.class)
public class ReversingIteratorTest {

    @Mock private Iterator<Object> iterator;

    private ReversingIterator reversingIterator;

    @Before
    public void setUp() {
        reversingIterator = new ReversingIterator<Object>(iterator);
    }

    @Test
    public void doesNotHaveNextIfIteratorIsEmpty() {
        when(iterator.hasNext()).thenReturn(false);
        assertThat(reversingIterator.hasNext(), is(false));
    }

    @Test
    public void reversesIterator() {
        mockIteratorToHaveValues(iterator, 1, 2, 3, 4, 5);

        assertThat(reversingIterator.next(), Is.<Object>is(5));
        assertThat(reversingIterator.next(), Is.<Object>is(4));
        assertThat(reversingIterator.next(), Is.<Object>is(3));
        assertThat(reversingIterator.next(), Is.<Object>is(2));
        assertThat(reversingIterator.next(), Is.<Object>is(1));
    }

    @Test
    public void doesNotReverseUntilNextIsCalled() {
        reversingIterator.hasNext();
        verify(iterator, never()).next();
    }

    @Test
    public void doesNotHaveNextIfFinishedReversingIterator() {
        mockIteratorToHaveValues(iterator, 1, 2, 3);
        reversingIterator.next();
        reversingIterator.next();
        reversingIterator.next();
        assertThat(reversingIterator.hasNext(), is(false));
    }

    @Test
    public void neverInteractsWithIteratorAgainAfterInitialReverse() {
        mockIteratorToHaveValues(iterator, 1, 2, 3);

        reversingIterator.next();
        verify(iterator, times(4)).hasNext();
        verify(iterator, times(3)).next();

        reversingIterator.next();
        verifyNoMoreInteractions(iterator);
    }
}
