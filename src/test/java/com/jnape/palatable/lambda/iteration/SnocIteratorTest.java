package com.jnape.palatable.lambda.iteration;

import org.junit.Before;
import org.junit.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SnocIteratorTest {

    private SnocIterator<Integer> iterator;

    @Before
    public void setUp() {
        iterator = new SnocIterator<>(asList(1, 2).iterator(), singletonList(3).iterator());
    }

    @Test
    public void hasNextIfInitNotYetIterated() {
        assertTrue(iterator.hasNext());
        assertEquals((Integer) 1, iterator.next());
    }

    @Test
    public void hasNextIfNoInitButLastNotYetIterated() {
        iterator.next();
        iterator.next();
        assertTrue(iterator.hasNext());
        assertEquals((Integer) 3, iterator.next());
    }

    @Test
    public void doesNotHaveNextIfLastIterated() {
        iterator.next();
        iterator.next();
        iterator.next();
        assertFalse(iterator.hasNext());
    }
}