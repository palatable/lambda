package com.jnape.palatable.lambda.iteration;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static testsupport.exceptions.OutOfScopeException.outOfScope;

public class InfiniteIteratorTest {

    private InfiniteIterator<?> infiniteIterator;

    @Before
    public void setUp() {
        infiniteIterator = new InfiniteIterator<Object>() {
            @Override
            public Object next() {
                throw outOfScope();
            }
        };
    }

    @Test
    public void alwaysHasNext() {
        assertThat(infiniteIterator.hasNext(), is(true));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void doesNotSupportRemove() {
        infiniteIterator.remove();
    }
}
