package com.jnape.palatable.lambda.internal.iteration;

import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CyclicIteratorTest {

    @Test
    public void alwaysHasNext() {
        CyclicIterator<Integer> cyclicIterator = new CyclicIterator<>(asList(1, 2).iterator());
        cyclicIterator.next();
        cyclicIterator.next();
        assertThat(cyclicIterator.hasNext(), is(true));
    }

    @Test
    public void cyclesThroughElements() {
        CyclicIterator<String> strings = new CyclicIterator<>(asList("bait", "switch").iterator());
        strings.next();
        strings.next();
        assertThat(strings.next(), is("bait"));
    }
}
