package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.monoid.Monoid;
import com.jnape.palatable.lambda.semigroup.Semigroup;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.ToMap.toMap;
import static com.jnape.palatable.lambda.monoid.builtin.MergeMaps.mergeMaps;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MergeMapsTest {
    private static final Semigroup<Integer> ADD = Integer::sum;

    private Monoid<Map<String, Integer>> merge;

    @Before
    public void setUp() {
        merge = mergeMaps(HashMap::new, ADD);
    }

    @Test
    public void identity() {
        assertTrue(merge.identity().isEmpty());
    }

    @Test
    public void monoid() {
        assertEquals(singletonMap("foo", 1), merge.apply(emptyMap(), singletonMap("foo", 1)));
        assertEquals(singletonMap("foo", 1), merge.apply(singletonMap("foo", 1), emptyMap()));
        assertEquals(singletonMap("foo", 2),
                     merge.apply(singletonMap("foo", 1), singletonMap("foo", 1)));
        assertEquals(toMap(HashMap::new, asList(tuple("foo", 1), tuple("bar", 1))),
                     merge.apply(singletonMap("foo", 1), singletonMap("bar", 1)));
    }
}