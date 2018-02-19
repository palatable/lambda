package com.jnape.palatable.lambda.iteration;

import org.junit.Test;

import java.util.Collections;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InitIteratorTest {

    @Test
    public void iteratesNothingForEmptyIterable() {
        assertFalse(new InitIterator<>(Collections::emptyIterator).hasNext());
    }

    @Test
    public void iteratesNothingForSingleElementIterable() {
        assertFalse(new InitIterator<>(singletonList(1)).hasNext());
    }

    @Test
    public void iteratesAllButLastElement() {
        InitIterator<Integer> iterator = new InitIterator<>(asList(1, 2, 3));

        assertTrue(iterator.hasNext());
        assertEquals((Integer) 1, iterator.next());

        assertTrue(iterator.hasNext());
        assertEquals((Integer) 2, iterator.next());

        assertFalse(iterator.hasNext());
    }
}