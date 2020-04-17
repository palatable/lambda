package com.jnape.palatable.lambda.internal.iteration;

import com.jnape.palatable.lambda.functions.specialized.Predicate;
import org.junit.Test;

import java.util.NoSuchElementException;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PredicatedTakingIteratorTest {

    public static final Predicate<String> HAS_FOUR_LETTERS = s -> s.length() == 4;

    @Test
    public void hasNextIfPredicateSucceedsForNextElement() {
        Iterable<String>                 words                    = asList("four", "three", "two", "one");
        PredicatedTakingIterator<String> predicatedTakingIterator = new PredicatedTakingIterator<>(HAS_FOUR_LETTERS, words.iterator());
        assertThat(predicatedTakingIterator.hasNext(), is(true));
    }

    @Test
    public void stopsTakingAfterFirstPredicateFailure() {
        Iterable<String>                 words                    = asList("once", "upon", "a", "time");
        PredicatedTakingIterator<String> predicatedTakingIterator = new PredicatedTakingIterator<>(HAS_FOUR_LETTERS, words.iterator());
        predicatedTakingIterator.next();
        predicatedTakingIterator.next();
        assertThat(predicatedTakingIterator.hasNext(), is(false));
    }

    @Test
    public void doesNotHaveNextIfFirstElementFailsPredicate() {
        Iterable<String>                 words                    = asList("I", "have", "a", "dream");
        PredicatedTakingIterator<String> predicatedTakingIterator = new PredicatedTakingIterator<>(HAS_FOUR_LETTERS, words.iterator());
        assertThat(predicatedTakingIterator.hasNext(), is(false));
    }

    @Test
    public void doesNotHaveNextIfTakenAllElements() {
        Iterable<String>                 words                    = asList("four", "four");
        PredicatedTakingIterator<String> predicatedTakingIterator = new PredicatedTakingIterator<>(HAS_FOUR_LETTERS, words.iterator());
        predicatedTakingIterator.next();
        predicatedTakingIterator.next();
        assertThat(predicatedTakingIterator.hasNext(), is(false));
    }

    @Test(expected = NoSuchElementException.class)
    public void throwsExceptionIfNextAfterFailedPredicate() {
        Iterable<String>                 words                    = singletonList("no");
        PredicatedTakingIterator<String> predicatedTakingIterator = new PredicatedTakingIterator<>(HAS_FOUR_LETTERS, words.iterator());
        predicatedTakingIterator.next();
    }

    @Test
    public void takesEverythingIfPredicateNeverFails() {
        Iterable<String>                 words                    = singletonList("yeah");
        PredicatedTakingIterator<String> predicatedTakingIterator = new PredicatedTakingIterator<>(HAS_FOUR_LETTERS, words.iterator());
        assertThat(predicatedTakingIterator.next(), is("yeah"));
        assertThat(predicatedTakingIterator.hasNext(), is(false));
    }
}
