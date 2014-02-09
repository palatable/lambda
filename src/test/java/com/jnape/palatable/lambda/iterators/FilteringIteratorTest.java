package com.jnape.palatable.lambda.iterators;

import com.jnape.palatable.lambda.Predicate;
import org.junit.Test;

import static java.lang.Character.toUpperCase;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FilteringIteratorTest {

    @Test
    public void hasNextIfAnyElementsHoldTrueForPredicate() {
        FilteringIterator<Integer> numbersBelowFive = new FilteringIterator<Integer>(
                new Predicate<Integer>() {
                    @Override
                    public Boolean apply(Integer integer) {
                        return integer < 5;
                    }
                },
                asList(5, 3, 10, 2, 4).iterator()
        );
        assertThat(numbersBelowFive.hasNext(), is(true));
    }

    @Test
    public void nextReturnsNextMatchingElement() {
        FilteringIterator<Character> letters = new FilteringIterator<Character>(
                new Predicate<Character>() {
                    @Override
                    public Boolean apply(Character character) {
                        return toUpperCase(character) == character;
                    }
                },
                asList('a', 'B', 'C').iterator()
        );
        assertThat(letters.next(), is('B'));
    }

    @Test
    public void doesNotHaveNextIfNoRemainingElementsHoldTrueForPredicate() {
        FilteringIterator<Integer> fiveThroughTen = new FilteringIterator<Integer>(
                new Predicate<Integer>() {
                    @Override
                    public Boolean apply(Integer integer) {
                        return integer < 5;
                    }
                },
                asList(5, 6, 7, 8, 9, 10).iterator()
        );
        assertThat(fiveThroughTen.hasNext(), is(false));
    }
}
