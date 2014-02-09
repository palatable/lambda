package com.jnape.palatable.lambda.iterators;

import org.junit.Test;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SingleValueCachingIteratorTest {

    @Test
    public void cachesElementMostRecentlyIteratedOver() {
        SingleValueCachingIterator<Integer> oneThroughFive = new SingleValueCachingIterator<Integer>(asList(1, 2, 3, 4, 5).iterator());
        oneThroughFive.next();
        assertThat(oneThroughFive.lastElementCached(), is(1));
    }

    @Test
    public void erasesCacheWhenAccessed() {
        SingleValueCachingIterator<String> words = new SingleValueCachingIterator<String>(asList("the", "rain", "in", "Spain").iterator());
        words.next();
        words.lastElementCached();
        assertThat(words.hasCachedElement(), is(false));
    }

    @Test(expected = NoSuchElementException.class)
    public void throwsExceptionIfRetrievingEmptyCache() {
        new SingleValueCachingIterator<String>(new ArrayList<String>().iterator()).lastElementCached();
    }
}
