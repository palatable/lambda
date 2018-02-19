package com.jnape.palatable.lambda.iteration;

import org.junit.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyIterator;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class FlatteningIteratorTest {

    @Test
    public void doesNotHaveNextForEmptyIterable() {
        FlatteningIterator<Object> iterator = new FlatteningIterator<>(emptyIterator());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void doesNotHaveNextForIterableOfEmptyIterables() {
        FlatteningIterator<Object> iterator = new FlatteningIterator<>(singletonList(emptyList()).iterator());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void iteratesNestedIterables() {
        FlatteningIterator<Object> iterator = new FlatteningIterator<>(asList(singletonList(1), asList(2,3), emptyList()).iterator());
        assertEquals(1, iterator.next());
        assertEquals(2, iterator.next());
        assertEquals(3, iterator.next());
        assertFalse(iterator.hasNext());
    }
}