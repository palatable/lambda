package com.jnape.palatable.lambda.traversable;

import org.junit.Test;

import java.util.Optional;

import static com.jnape.palatable.lambda.traversable.Traversables.traversable;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;

public class TraversablesTest {

    @Test
    public void staticFactoryMethods() {
        Iterable<Integer> ints = asList(1, 2, 3);
        assertEquals(TraversableIterable.wrap(ints), traversable(ints));
        assertEquals(TraversableIterable.empty(), traversable(emptyList()));

        Optional<String> optString = Optional.of("foo");
        assertEquals(TraversableOptional.wrap(optString), traversable(optString));
        assertEquals(TraversableOptional.empty(), traversable(Optional.empty()));
    }
}