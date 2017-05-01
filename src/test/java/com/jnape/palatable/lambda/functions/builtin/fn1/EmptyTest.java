package com.jnape.palatable.lambda.functions.builtin.fn1;

import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Empty.empty;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Repeat.repeat;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EmptyTest {

    @Test
    public void emptiness() {
        Empty<Object> empty = empty();

        assertTrue(empty.apply(emptySet()));
        assertFalse(empty.apply(singleton(1)));
        assertFalse(empty.apply(repeat(1)));
    }
}