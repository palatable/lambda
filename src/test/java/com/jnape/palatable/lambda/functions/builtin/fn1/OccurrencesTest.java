package com.jnape.palatable.lambda.functions.builtin.fn1;

import org.junit.Test;

import java.util.HashMap;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Occurrences.occurrences;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static org.junit.Assert.assertEquals;

public class OccurrencesTest {

    @Test
    public void occurrencesOfIndividualElements() {
        assertEquals(new HashMap<String, Long>() {{
            put("foo", 2L);
            put("bar", 2L);
            put("baz", 1L);
        }}, occurrences(asList("foo", "bar", "foo", "baz", "bar")));
    }

    @Test
    public void emptyIterableHasNoOccurrences() {
        assertEquals(emptyMap(), occurrences(emptyList()));
    }
}
