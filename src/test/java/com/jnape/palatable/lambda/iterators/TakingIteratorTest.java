package com.jnape.palatable.lambda.iterators;

import org.junit.Test;

import static com.jnape.palatable.lambda.staticfactory.IterableFactory.iterable;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TakingIteratorTest {

    @Test
    public void doesNotHaveNextIfTakenAllElements() {
        TakingIterator<Integer> takingIterator = new TakingIterator<Integer>(3, iterable(1, 2, 3, 4).iterator());
        takingIterator.next();
        takingIterator.next();
        takingIterator.next();
        assertThat(takingIterator.hasNext(), is(false));
    }

    @Test
    public void doesNotHaveNextIfOutOfElements() {
        TakingIterator<Integer> takingIterator = new TakingIterator<Integer>(5, iterable(1).iterator());
        takingIterator.next();
        assertThat(takingIterator.hasNext(), is(false));
    }
}
