package com.jnape.palatable.lambda.lens.lenses;

import com.jnape.palatable.lambda.lens.Lens;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.lens.functions.Set.set;
import static com.jnape.palatable.lambda.lens.functions.View.view;
import static com.jnape.palatable.lambda.lens.lenses.MapLens.keys;
import static com.jnape.palatable.lambda.lens.lenses.MapLens.mappingValues;
import static com.jnape.palatable.lambda.lens.lenses.MapLens.valueAt;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonMap;
import static java.util.Collections.unmodifiableMap;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;
import static testsupport.assertion.LensAssert.assertLensLawfulness;

public class MapLensTest {

    @Test
    public void asCopyFocusesOnMapThroughCopy() {
        assertLensLawfulness(MapLens.asCopy(),
                             asList(emptyMap(), singletonMap("foo", 1), new HashMap<String, Integer>() {{
                                 put("foo", 1);
                                 put("bar", 2);
                                 put("baz", 3);
                             }}),
                             asList(emptyMap(), singletonMap("foo", 1), new HashMap<String, Integer>() {{
                                 put("foo", 1);
                                 put("bar", 2);
                                 put("baz", 3);
                             }}));
    }

    @Test
    public void valueAtFocusesOnValueAtKey() {
        assertLensLawfulness(valueAt("foo"),
                             asList(emptyMap(), singletonMap("foo", 1), new HashMap<String, Integer>() {{
                                 put("foo", 1);
                                 put("bar", 2);
                                 put("baz", 3);
                             }}),
                             asList(nothing(), just(1)));
    }

    @Test
    public void valueAtWithDefaultValueFocusedOnValueAtKey() {
        Lens.Simple<Map<String, Integer>, Integer> atFoo = valueAt("foo", -1);

        assertEquals((Integer) 1, view(atFoo, new HashMap<String, Integer>() {{
            put("foo", 1);
            put("bar", 2);
            put("baz", 3);
        }}));
        assertEquals((Integer) (-1), view(atFoo, emptyMap()));

        Map<String, Integer> updated = set(atFoo, 11, new HashMap<String, Integer>() {{
            put("foo", 1);
            put("bar", 2);
            put("baz", 3);
        }});
        assertEquals(new HashMap<String, Integer>() {{
            put("foo", 11);
            put("bar", 2);
            put("baz", 3);
        }}, updated);
        assertNotSame(new HashMap<String, Integer>() {{
            put("foo", 1);
            put("bar", 2);
            put("baz", 3);
        }}, updated);
    }

    @Test
    public void keysFocusesOnKeys() {
        assertLensLawfulness(keys(),
                             asList(emptyMap(), singletonMap("foo", 1), new HashMap<String, Integer>() {{
                                 put("foo", 1);
                                 put("bar", 2);
                                 put("baz", 3);
                             }}),
                             asList(emptySet(), singleton("foo"), new HashSet<>(asList("foo", "bar", "baz", "quux")), new HashSet<>(asList("foo", "baz", "quux"))));
    }

    @Test
    public void valuesFocusesOnValues() {
        Lens.Simple<Map<String, Integer>, Collection<Integer>> values = MapLens.values();

        assertThat(view(values, new HashMap<String, Integer>() {{
            put("foo", 1);
            put("bar", 2);
            put("baz", 3);
        }}), hasItems(2, 1, 3));

        Map<String, Integer> updated = set(values, asList(1, 2), new HashMap<String, Integer>() {{
            put("foo", 1);
            put("bar", 2);
            put("baz", 3);
        }});
        assertEquals(new HashMap<String, Integer>() {{
            put("foo", 1);
            put("bar", 2);
        }}, updated);
        assertNotSame(new HashMap<String, Integer>() {{
            put("foo", 1);
            put("bar", 2);
            put("baz", 3);
        }}, updated);
    }

    @Test
    public void invertedFocusesOnMapWithKeysAndValuesSwitched() {
        assertLensLawfulness(MapLens.inverted(),
                             asList(emptyMap(), singletonMap("foo", 1), new HashMap<String, Integer>() {{
                                 put("foo", 1);
                                 put("bar", 2);
                                 put("baz", 3);
                             }}),
                             asList(emptyMap(), singletonMap(1, "foo"), new HashMap<Integer, String>() {{
                                 put(1, "foo");
                                 put(2, "bar");
                                 put(3, "baz");
                             }}));
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