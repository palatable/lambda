package com.jnape.palatable.lambda.optics.lenses;

import com.jnape.palatable.lambda.optics.Lens;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.optics.Iso.iso;
import static com.jnape.palatable.lambda.optics.functions.Set.set;
import static com.jnape.palatable.lambda.optics.functions.View.view;
import static com.jnape.palatable.lambda.optics.lenses.MapLens.keys;
import static com.jnape.palatable.lambda.optics.lenses.MapLens.mappingValues;
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
import static testsupport.matchers.IterableMatcher.iterates;

@SuppressWarnings("serial")
public class MapLensTest {

    @Test
    public void asCopy() {
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
    public void asCopyWithCopyFn() {
        assertLensLawfulness(MapLens.asCopy(LinkedHashMap::new),
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

        assertThat(view(MapLens.asCopy(LinkedHashMap::new), new LinkedHashMap<String, Integer>() {{
            put("foo", 1);
            put("bar", 2);
            put("baz", 3);
        }}).keySet(), iterates("foo", "bar", "baz"));
    }

    @Test
    public void valueAt() {
        assertLensLawfulness(MapLens.valueAt("foo"),
                             asList(emptyMap(), singletonMap("foo", 1), new HashMap<String, Integer>() {{
                                 put("foo", 1);
                                 put("bar", 2);
                                 put("baz", 3);
                             }}),
                             asList(nothing(), just(1)));
    }

    @Test
    public void valueAtWithCopyFn() {
        assertLensLawfulness(MapLens.valueAt("foo"),
                             asList(emptyMap(), singletonMap("foo", 1), new HashMap<String, Integer>() {{
                                 put("foo", 1);
                                 put("bar", 2);
                                 put("baz", 3);
                             }}),
                             asList(nothing(), just(1)));
    }


    @Test
    public void valueAtWithDefaultValue() {
        Lens.Simple<Map<String, Integer>, Integer> atFoo = MapLens.valueAt("foo", -1);

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
    public void mappingValuesWithIsoRetainsMapStructureWithMappedValues() {
        assertLensLawfulness(mappingValues(iso(Integer::parseInt, Object::toString)),
                             asList(emptyMap(),
                                    singletonMap("foo", "1"),
                                     Map.of("foo", "1", "bar", "2", "baz", "3")),
                             asList(emptyMap(),
                                    singletonMap("foo", 1),
                                     Map.of("foo", 1, "bar", 2, "baz", 3)));
    }
}