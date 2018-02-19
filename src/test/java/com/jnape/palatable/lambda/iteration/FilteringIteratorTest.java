package com.jnape.palatable.lambda.iteration;

import org.junit.Test;

import static java.lang.Character.toUpperCase;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FilteringIteratorTest {

    @Test
    public void hasNextIfAnyElementsHoldTrueForPredicate() {
        FilteringIterator<Integer> numbersBelowFive = new FilteringIterator<>(
                x -> x < 5,
                asList(5, 3, 10, 2, 4).iterator()
        );

        assertThat(numbersBelowFive.hasNext(), is(true));
    }

    @Test
    public void nextReturnsNextMatchingElement() {
        FilteringIterator<Character> letters = new FilteringIterator<>(
                x -> toUpperCase(x) == x,
                asList('a', 'B', 'C').iterator()
        );

        assertThat(letters.next(), is('B'));
    }

    @Test
    public void doesNotHaveNextIfNoRemainingElementsHoldTrueForPredicate() {
        FilteringIterator<Integer> fiveThroughTen = new FilteringIterator<>(
                x -> x < 5,
                asList(5, 6, 7, 8, 9, 10).iterator()
        );

        assertThat(fiveThroughTen.hasNext(), is(false));
    }
}
