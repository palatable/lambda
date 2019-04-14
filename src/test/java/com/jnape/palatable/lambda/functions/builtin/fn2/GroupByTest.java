package com.jnape.palatable.lambda.functions.builtin.fn2;

import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn2.GroupBy.groupBy;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

@SuppressWarnings("serial")
public class GroupByTest {

    @Test
    public void grouping() {
        assertEquals(new HashMap<Integer, List<String>>() {{
            put(3, asList("one", "two"));
            put(5, singletonList("three"));
        }}, groupBy(String::length, asList("one", "two", "three")));
    }

    @Test
    public void emptyIterableProducesEmptyMap() {
        assertEquals(Collections.<Object, List<Object>>emptyMap(), groupBy(id(), emptyList()));
    }
}