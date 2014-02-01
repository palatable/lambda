package com.jnape.palatable.lambda.iterables;

import org.junit.Test;

import java.util.Iterator;

import static com.jnape.palatable.lambda.iterables.ReversingIterable.reverse;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static testsupport.matchers.IterableMatcher.iterates;

public class ReversingIterableTest {

    @Test
    public void iteratesElementsOfAnIterableBackwards() {
        Iterable<String> words = asList("the", "rain", "in", "Spain");
        ReversingIterable<String> reversed = reverse(words);

        assertThat(reversed, iterates("Spain", "in", "rain", "the"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void doesNotBeginReversingUntilIterated() {
        Iterable<Integer> mockIterable = mock(Iterable.class);
        Iterator<Integer> mockIterator = mock(Iterator.class);

        when(mockIterable.iterator()).thenReturn(mockIterator);

        Iterator<Integer> lazyIterable = reverse(mockIterable).iterator();
        lazyIterable.hasNext();

        verify(mockIterator).hasNext();
        verify(mockIterator, never()).next();
    }
}
