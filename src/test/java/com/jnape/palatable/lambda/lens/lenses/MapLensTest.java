package com.jnape.palatable.lambda.lens.lenses;

import com.jnape.palatable.lambda.lens.Lens;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.jnape.palatable.lambda.lens.functions.Set.set;
import static com.jnape.palatable.lambda.lens.functions.View.view;
import static com.jnape.palatable.lambda.lens.lenses.MapLens.keys;
import static com.jnape.palatable.lambda.lens.lenses.MapLens.mappingValues;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static java.util.Collections.unmodifiableMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public class MapLensTest {

    private Map<String, Integer> m;

    @Before
    public void setUp() {
        m = new HashMap<String, Integer>() {{
            put("foo", 1);
            put("bar", 2);
            put("baz", 3);
        }};
    }

    @Test
    public void asCopyFocusesOnMapThroughCopy() {
        Lens.Simple<Map<String, Integer>, Map<String, Integer>> asCopy = MapLens.asCopy();

        assertEquals(m, view(asCopy, m));
        assertNotSame(m, view(asCopy, m));

        Map<String, Integer> update = new HashMap<String, Integer>() {{
            put("quux", 0);
        }};
        assertSame(update, set(asCopy, update, m));
    }

    @Test
    public void valueAtFocusesOnValueAtKey() {
        Lens<Map<String, Integer>, Map<String, Integer>, Optional<Integer>, Integer> atFoo = MapLens.valueAt("foo");

        assertEquals(Optional.of(1), view(atFoo, m));

        Map<String, Integer> updated = set(atFoo, -1, m);
        assertEquals(new HashMap<String, Integer>() {{
            put("foo", -1);
            put("bar", 2);
            put("baz", 3);
        }}, updated);
        assertSame(m, updated);
    }

    @Test
    public void valueAtWithDefaultValueFocusedOnValueAtKey() {
        Lens<Map<String, Integer>, Map<String, Integer>, Integer, Integer> atFoo = MapLens.valueAt("foo", -1);

        assertEquals((Integer) 1, view(atFoo, m));
        assertEquals((Integer) (-1), view(atFoo, emptyMap()));

        Map<String, Integer> updated = set(atFoo, 11, m);
        assertEquals(new HashMap<String, Integer>() {{
            put("foo", 11);
            put("bar", 2);
            put("baz", 3);
        }}, updated);
        assertSame(m, updated);
    }

    @Test
    public void keysFocusesOnKeys() {
        Lens<Map<String, Integer>, Map<String, Integer>, Set<String>, Set<String>> keys = keys();

        assertEquals(m.keySet(), view(keys, m));

        Map<String, Integer> updated = set(keys, new HashSet<>(asList("bar", "baz", "quux")), m);
        assertEquals(new HashMap<String, Integer>() {{
            put("bar", 2);
            put("baz", 3);
            put("quux", null);
        }}, updated);
        assertSame(m, updated);
    }

    @Test
    public void valuesFocusesOnValues() {
        Lens<Map<String, Integer>, Map<String, Integer>, Collection<Integer>, Collection<Integer>> values = MapLens.values();

        assertEquals(m.values(), view(values, m));

        Map<String, Integer> updated = set(values, asList(1, 2), m);
        assertEquals(new HashMap<String, Integer>() {{
            put("foo", 1);
            put("bar", 2);
        }}, updated);
        assertSame(m, updated);
    }

    @Test
    public void invertedFocusesOnMapWithKeysAndValuesSwitched() {
        Lens.Simple<Map<String, Integer>, Map<Integer, String>> inverted = MapLens.inverted();

        assertEquals(new HashMap<Integer, String>() {{
            put(1, "foo");
            put(2, "bar");
            put(3, "baz");
        }}, view(inverted, m));

        Map<String, Integer> updated = set(inverted, new HashMap<Integer, String>() {{
            put(2, "bar");
            put(3, "baz");
        }}, m);
        assertEquals(new HashMap<String, Integer>() {{
            put("bar", 2);
            put("baz", 3);
        }}, updated);
        assertSame(m, updated);

        Map<String, Integer> withDuplicateValues = new HashMap<String, Integer>() {{
            put("foo", 1);
            put("bar", 1);
        }};
        assertEquals(new HashMap<Integer, String>() {{
            put(1, "foo");
        }}, view(inverted, withDuplicateValues));
    }

    @Test
    public void mappingValuesRetainsMapStructureWithMappedValues() {
        Map<String, String> m = unmodifiableMap(new HashMap<String, String>() {{
            put("foo", "1");
            put("bar", "2");
            put("baz", "3");
        }});
        Lens.Simple<Map<String, String>, Map<String, Integer>> mappingValues = mappingValues(Integer::parseInt);

        assertEquals(new HashMap<String, Integer>() {{
            put("foo", 1);
            put("bar", 2);
            put("baz", 3);
        }}, view(mappingValues, m));

        Map<String, String> updated = set(mappingValues, unmodifiableMap(new HashMap<String, Integer>() {{
            put("foo", 2);
            put("bar", 1);
            put("baz", 3);
        }}), m);
        assertEquals(singletonMap("baz", "3"), updated);
    }
}