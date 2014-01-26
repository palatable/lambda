package com.jnape.palatable.lambda.iterators;

import org.junit.Before;
import org.junit.Test;

public class ImmutableIteratorTest {

    private ImmutableIterator immutableIterator;

    @Before
    public void setUp() {
        immutableIterator = new ImmutableIterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };
    }

    @Test(expected = UnsupportedOperationException.class)
    public void doesNotSupportRemove() {
        immutableIterator.remove();
    }
}
