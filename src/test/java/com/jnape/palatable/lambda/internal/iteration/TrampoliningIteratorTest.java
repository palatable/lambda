package com.jnape.palatable.lambda.internal.iteration;

import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.recursion.RecursiveResult.recurse;
import static com.jnape.palatable.lambda.functions.recursion.RecursiveResult.terminate;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TrampoliningIteratorTest {

    @Test
    public void hasNextIfAnyTerminateInstructions() {
        TrampoliningIterator<Integer, Object> it = new TrampoliningIterator<>(x -> singleton(terminate(x + 1)), 0);
        assertTrue(it.hasNext());
        assertEquals(1, it.next());
        assertFalse(it.hasNext());
    }

    @Test
    public void hasNextIfTerminateInterleavedBeforeRecurse() {
        TrampoliningIterator<Integer, Object> it = new TrampoliningIterator<>(
                x -> x < 3
                     ? asList(terminate(x), recurse(x + 1))
                     : emptyList(),
                0);
        assertTrue(it.hasNext());
        assertEquals(0, it.next());
        assertTrue(it.hasNext());
        assertEquals(1, it.next());
        assertTrue(it.hasNext());
        assertEquals(2, it.next());
        assertFalse(it.hasNext());
    }

    @Test
    public void hasNextIfTerminateInterleavedAfterRecurse() {
        TrampoliningIterator<Integer, Object> it = new TrampoliningIterator<>(
                x -> x < 3
                     ? asList(recurse(x + 1), terminate(x))
                     : emptyList(),
                0);
        assertTrue(it.hasNext());
        assertEquals(2, it.next());
        assertTrue(it.hasNext());
        assertEquals(1, it.next());
        assertTrue(it.hasNext());
        assertEquals(0, it.next());
        assertFalse(it.hasNext());
    }

    @Test
    public void doesNotHaveNextIfEmptyInitialResult() {
        TrampoliningIterator<Integer, Object> it = new TrampoliningIterator<>(constantly(emptyList()), 0);
        assertFalse(it.hasNext());
    }

    @Test
    public void doesNotHaveNextIfNoTerminateInstruction() {
        TrampoliningIterator<Integer, Object> it = new TrampoliningIterator<>(
                x -> x < 3
                     ? singleton(recurse(x + 1))
                     : emptyList(),
                0);
        assertFalse(it.hasNext());
    }
}