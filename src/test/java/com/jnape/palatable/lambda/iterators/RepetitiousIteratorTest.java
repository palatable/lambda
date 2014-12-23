package com.jnape.palatable.lambda.iterators;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class RepetitiousIteratorTest {

    private RepetitiousIterator<Integer> repetitiousIterator;

    @Before
    public void setUp() {
        repetitiousIterator = new RepetitiousIterator<>(1);
    }

    @Test
    public void alwaysHasNext() {
        assertThat(repetitiousIterator.hasNext(), is(true));
        repetitiousIterator.next();
        assertThat(repetitiousIterator.hasNext(), is(true));
    }

    @Test
    public void iteratesSingleValue() {
        assertThat(repetitiousIterator.next(), is(1));
        assertThat(repetitiousIterator.next(), is(1));
        assertThat(repetitiousIterator.next(), is(1));
    }
}
