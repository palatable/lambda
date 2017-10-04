package com.jnape.palatable.lambda.iterators;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Optional;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Last.last;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Iterate.iterate;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Take.take;
import static com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft.foldLeft;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SnocIteratorTest {

    private SnocIterator<Integer> iterator;

    @Before
    public void setUp() {
        iterator = new SnocIterator<>(3, asList(1, 2));
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

    @Test
    public void stackSafety() {
        int stackBlowingNumber = 100000;
        Iterable<Integer> ints = foldLeft((xs, x) -> () -> new SnocIterator<>(x, xs),
                                          Collections::emptyIterator,
                                          take(stackBlowingNumber, iterate(x -> x + 1, 1)));

        assertEquals(Optional.of(stackBlowingNumber), last(ints));
    }
}