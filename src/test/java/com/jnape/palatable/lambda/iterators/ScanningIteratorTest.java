package com.jnape.palatable.lambda.iterators;

import com.jnape.palatable.lambda.functions.Fn2;
import org.junit.Test;

import java.util.NoSuchElementException;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyIterator;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ScanningIteratorTest {

    public static final Fn2<Integer, Integer, Integer> ADD = (x, y) -> x + y;

    @Test
    public void hasNextAtLeastForB() {
        ScanningIterator<Integer, Integer> scanningIterator = new ScanningIterator<>(ADD, 0, emptyIterator());
        assertThat(scanningIterator.hasNext(), is(true));
    }

    @Test
    public void nextIsTrueAtLeastForB() {
        ScanningIterator<Integer, Integer> scanningIterator = new ScanningIterator<>(ADD, 0, emptyIterator());
        assertThat(scanningIterator.next(), is(0));
    }

    @Test
    public void iteratesAsAfterB() {
        ScanningIterator<Integer, Integer> scanningIterator = new ScanningIterator<>(ADD, 0, asList(1, 2, 3, 4, 5).iterator());

        scanningIterator.next();
        assertThat(scanningIterator.hasNext(), is(true));
        assertThat(scanningIterator.next(), is(ADD.apply(0, 1)));
    }

    @Test(expected = NoSuchElementException.class)
    public void failsOnNextIfOutOfAs() {
        ScanningIterator<Integer, Integer> scanningIterator = new ScanningIterator<>(ADD, 0, emptyIterator());
        scanningIterator.next();
        scanningIterator.next();
    }
}