package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.iterators.FilteringIterator;
import org.junit.Test;

import static com.jnape.palatable.lambda.builtin.monadic.Always.always;
import static com.jnape.palatable.lambda.functions.Filter.filter;
import static com.jnape.palatable.lambda.staticfactory.IterableFactory.iterable;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class FilterTest {

    @Test
    public void producesFilteringIterator() {
        assertThat(
                filter(always(true), iterable(1, 2, 3)).iterator(),
                is(instanceOf(FilteringIterator.class))
        );
    }
}
