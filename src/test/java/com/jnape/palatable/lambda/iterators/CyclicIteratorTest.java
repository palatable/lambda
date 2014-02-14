package com.jnape.palatable.lambda.iterators;

import org.junit.Test;

import static com.jnape.palatable.lambda.staticfactory.IterableFactory.iterable;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CyclicIteratorTest {

    @Test
    public void alwaysHasNext() {
        CyclicIterator<Integer> cyclicIterator = new CyclicIterator<Integer>(iterable(1, 2).iterator());
        cyclicIterator.next();
        cyclicIterator.next();
        assertThat(cyclicIterator.hasNext(), is(true));
    }

    @Test
    public void cyclesThroughElements() {
        CyclicIterator<String> strings = new CyclicIterator<String>(iterable("bait", "switch").iterator());
        strings.next();
        strings.next();
        assertThat(strings.next(), is("bait"));
    }
}
