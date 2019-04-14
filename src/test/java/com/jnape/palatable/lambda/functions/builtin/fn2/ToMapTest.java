package com.jnape.palatable.lambda.functions.builtin.fn2;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.ToMap.toMap;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class ToMapTest {

    @Test
    @SuppressWarnings("serial")
    public void collectsEntriesIntoMap() {
        Map<String, Integer> expected = new HashMap<String, Integer>() {{
            put("foo", 1);
            put("bar", 2);
            put("baz", 3);
        }};

        assertEquals(expected, toMap().apply(HashMap::new, asList(tuple("foo", 1), tuple("bar", 2), tuple("baz", 3))));
    }
}