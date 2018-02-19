package com.jnape.palatable.lambda.iteration;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TakingIteratorTest {

    @Test
    public void hasNextBeforeTakingAnyElements() {
        List<Integer> numbers = asList(1, 2, 3, 4, 5);
        TakingIterator<Integer> takingIterator = new TakingIterator<>(3, numbers.iterator());
        assertThat(takingIterator.hasNext(), is(true));
    }

    @Test
    public void doesNotHaveNextIfTakenEnoughElements() {
        List<Character> vowels = asList('a', 'e', 'i', 'o', 'u');
        TakingIterator<Character> takingIterator = new TakingIterator<>(3, vowels.iterator());
        takingIterator.next();
        takingIterator.next();
        takingIterator.next();
        assertThat(takingIterator.hasNext(), is(false));
    }

    @Test
    public void doesNotHaveNextIfTakenAllOfIterable() {
        List<String> words = asList("we", "the", "people");
        TakingIterator<String> takingIterator = new TakingIterator<>(4, words.iterator());
        takingIterator.next();
        takingIterator.next();
        takingIterator.next();
        assertThat(takingIterator.hasNext(), is(false));
    }

    @Test
    public void doesNotHaveNextForEmptyIterable() {
        TakingIterator<Object> takingIterator = new TakingIterator<>(3, emptyList().iterator());
        assertThat(takingIterator.hasNext(), is(false));
    }
}
