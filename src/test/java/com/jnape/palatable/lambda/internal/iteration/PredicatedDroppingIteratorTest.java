package com.jnape.palatable.lambda.internal.iteration;

import com.jnape.palatable.lambda.functions.Fn1;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static testsupport.Mocking.mockIteratorToHaveValues;

@RunWith(MockitoJUnitRunner.class)
public class PredicatedDroppingIteratorTest {

    private static final Fn1<? super Integer, Boolean> EVEN = x -> x % 2 == 0;

    @Mock private Iterator<Integer> iterator;

    private PredicatedDroppingIterator<Integer> predicatedDroppingIterator;

    @Before
    public void setUp() {
        predicatedDroppingIterator = new PredicatedDroppingIterator<>(EVEN, iterator);
    }

    @Test
    public void hasNextIfAnyElementsFailForPredicate() {
        mockIteratorToHaveValues(iterator, 0, 2, 4, 5, 6);
        assertThat(predicatedDroppingIterator.hasNext(), is(true));
    }

    @Test
    public void doesNotHaveNextIfNoElementsFailPredicate() {
        mockIteratorToHaveValues(iterator, 0, 2, 4, 6);
        assertThat(predicatedDroppingIterator.hasNext(), is(false));
    }

    @Test
    public void beginsIteratingAtFirstElementThatFailedPredicate() {
        mockIteratorToHaveValues(iterator, 0, 2, 4, 5, 6);

        assertThat(predicatedDroppingIterator.next(), is(5));
        assertThat(predicatedDroppingIterator.next(), is(6));
    }

    @Test(expected = NoSuchElementException.class)
    public void failsIfOnNextNoElementsFailedPredicate() {
        mockIteratorToHaveValues(iterator, 0, 2, 4, 6);
        predicatedDroppingIterator.next();
    }

    @Test
    public void iteratesAllElementsIfFirstElementFailedPredicate() {
        mockIteratorToHaveValues(iterator, -1, 0, 2, 4, 6);
        assertThat(predicatedDroppingIterator.next(), is(-1));
        assertThat(predicatedDroppingIterator.next(), is(0));
        assertThat(predicatedDroppingIterator.next(), is(2));
        assertThat(predicatedDroppingIterator.next(), is(4));
        assertThat(predicatedDroppingIterator.next(), is(6));
    }
}
