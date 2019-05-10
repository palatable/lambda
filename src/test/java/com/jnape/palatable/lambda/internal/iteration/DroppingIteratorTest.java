package com.jnape.palatable.lambda.internal.iteration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Iterator;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static testsupport.Mocking.mockIteratorToHaveValues;

@RunWith(MockitoJUnitRunner.class)
public class DroppingIteratorTest {

    @Mock private Iterator<Object> iterator;

    private DroppingIterator<?> droppingIterator;

    @Before
    public void setUp() {
        droppingIterator = new DroppingIterator<>(5, iterator);
    }

    @Test
    public void doesNotHaveNextIfIteratorHoldsNOrLessElements() {
        mockIteratorToHaveValues(iterator, 1, 2, 3, 4, 5);
        assertThat(droppingIterator.hasNext(), is(false));
    }

    @Test
    public void hasNextIfIteratorHoldsMoreThanNElements() {
        mockIteratorToHaveValues(iterator, 1, 2, 3, 4, 5, 6);
        assertThat(droppingIterator.hasNext(), is(true));
    }

    @Test
    public void dropsElementsOnNextIfNotAlreadyDropped() {
        mockIteratorToHaveValues(iterator, 1, 2, 3, 4, 5, 6);
        assertThat(droppingIterator.next(), is(6));
    }
}
