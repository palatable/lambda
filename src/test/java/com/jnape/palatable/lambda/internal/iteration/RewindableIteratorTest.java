package com.jnape.palatable.lambda.internal.iteration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static testsupport.Mocking.mockIteratorToHaveValues;

@RunWith(MockitoJUnitRunner.class)
public class RewindableIteratorTest {

    @Mock private Iterator<Object> iterator;

    private RewindableIterator<?> rewindableIterator;

    @Before
    public void setUp() {
        rewindableIterator = new RewindableIterator<>(iterator);
    }

    @Test
    public void rewindingQueuesPreviousElementUpForNext() {
        mockIteratorToHaveValues(iterator, 1, 2, 3);
        rewindableIterator.next();
        rewindableIterator.next();
        rewindableIterator.rewind();

        assertThat(rewindableIterator.next(), is(2));
    }

    @Test
    public void hasNextIfFullyIteratedButRewound() {
        mockIteratorToHaveValues(iterator, 1, 2, 3);
        rewindableIterator.next();
        rewindableIterator.next();
        rewindableIterator.next();
        rewindableIterator.rewind();

        assertThat(rewindableIterator.hasNext(), is(true));
    }

    @Test(expected = NoSuchElementException.class)
    public void cannotRewindIfNoValuesIterated() {
        rewindableIterator.rewind();
    }

    @Test(expected = NoSuchElementException.class)
    public void cannotRewindTheSameElementTwice() {
        mockIteratorToHaveValues(iterator, 1, 2, 3);
        rewindableIterator.next();
        rewindableIterator.rewind();
        rewindableIterator.next();
        rewindableIterator.rewind();
    }

    @Test
    @SuppressWarnings("Duplicates")
    public void doesNotHaveNextIfNoMoreElementsAndIsNotRewound() {
        mockIteratorToHaveValues(iterator, 1, 2, 3);
        rewindableIterator.next();
        rewindableIterator.next();
        rewindableIterator.next();
        assertThat(rewindableIterator.hasNext(), is(false));
    }
}
