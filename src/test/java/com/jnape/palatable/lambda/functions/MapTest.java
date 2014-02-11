package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.iterators.MappingIterator;
import org.junit.Test;

import static com.jnape.palatable.lambda.builtin.monadic.Always.always;
import static com.jnape.palatable.lambda.functions.Map.map;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MapTest {

    @Test
    public void producesMappingIterator() {
        assertThat(
                map(always(true), asList("a", "b", "c")).iterator(),
                is(instanceOf(MappingIterator.class))
        );
    }
}
