package com.jnape.palatable.lambda.internal.iteration;

import org.junit.Test;

import java.util.NoSuchElementException;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyIterator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class PrependingIteratorTest {

    @Test
    public void empty() {
        PrependingIterator<Integer> iterator = new PrependingIterator<>(0, emptyIterator());
        assertFalse(iterator.hasNext());

        assertThrows(
            NoSuchElementException.class,
            iterator::next
        );
    }

    @Test
    public void nonEmpty() {
        PrependingIterator<Integer> iterator = new PrependingIterator<>(0, asList(1, 2, 3).iterator());

        assertTrue(iterator.hasNext());
        assertEquals((Integer) 0, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals((Integer) 1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals((Integer) 0, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals((Integer) 2, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals((Integer) 0, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals((Integer) 3, iterator.next());
        assertFalse(iterator.hasNext());
    }
}