package com.jnape.palatable.lambda.lens.lenses;

import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.lens.Lens;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.jnape.palatable.lambda.lens.functions.Set.set;
import static com.jnape.palatable.lambda.lens.functions.View.view;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static java.util.Collections.unmodifiableMap;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

public class MapLensTest {

    private Map<String, Integer> m;

    @Before
    public void setUp() throws Exception {
        m = unmodifiableMap(new HashMap<String, Integer>() {{
            put("foo", 1);
            put("bar", 2);
            put("baz", 3);
        }});
    }

    @Test
    public void asCopyFocusesOnMapThroughCopy() {
        Lens.Simple<Map<String, Integer>, Map<String, Integer>> asCopy = MapLens.asCopy();

        assertEquals(m, view(asCopy, m));
        assertNotSame(m, view(asCopy, m));

        Map<String, Integer> update = new HashMap<String, Integer>() {{
            put("quux", 0);
        }};
        assertEquals(update, set(asCopy, update, m));
        assertSame(update, set(asCopy, update, m));
    }

    @Test
    public void atKeyFocusesOnValueAtKey() {
        Lens.Simple<Map<String, Integer>, Integer> atFoo = MapLens.atKey("foo");

        assertEquals((Integer) 1, view(atFoo, m));
        assertEquals(new HashMap<String, Integer>() {{
            put("foo", -1);
            put("bar", 2);
            put("baz", 3);
        }}, set(atFoo, -1, m));
    }

    @Test
    public void keysFocusesOnKeysWithImmutableSet() {
        Lens.Simple<Map<String, Integer>, Set<String>> keys = MapLens.keys();

        assertEquals(m.keySet(), view(keys, m));
        view(keys, m).clear();
        assertEquals(new HashSet<>(asList("foo", "bar", "baz")), m.keySet());

        assertEquals(new HashMap<String, Integer>() {{
            put("bar", 2);
            put("baz", 3);
            put("quux", null);
        }}, set(keys, new HashSet<>(asList("bar", "baz", "quux")), m));
    }

    @Test
    public void valuesFocusesOnValues() {
        Lens<Map<String, Integer>, Map<String, Integer>, Collection<Integer>, Fn2<String, Integer, Integer>> values = MapLens.values();

        assertThat(view(values, m),
                   containsInAnyOrder(m.values().toArray()));

        assertEquals(new HashMap<String, Integer>() {{
            put("foo", 4);
            put("bar", 5);
            put("baz", 6);
        }}, set(values, (k, v) -> k.length() + v, m));
    }

    @Test
    public void invertedFocusesOnMapWithKeysAndValuesSwitched() {
        Lens.Simple<Map<String, Integer>, Map<Integer, String>> inverted = MapLens.inverted();

        assertEquals(new HashMap<Integer, String>() {{
            put(1, "foo");
            put(2, "bar");
            put(3, "baz");
        }}, view(inverted, m));

        assertEquals(new HashMap<String, Integer>() {{
            put("quux", -1);
        }}, set(inverted, singletonMap(-1, "quux"), m));
    }
}