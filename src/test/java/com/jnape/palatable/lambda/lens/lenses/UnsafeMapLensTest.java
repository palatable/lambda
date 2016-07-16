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

import static com.jnape.palatable.lambda.lens.functions.Over.over;
import static com.jnape.palatable.lambda.lens.functions.Set.set;
import static com.jnape.palatable.lambda.lens.functions.View.view;
import static com.jnape.palatable.lambda.lens.lenses.UnsafeMapLens.keys;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class UnsafeMapLensTest {

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
    public void atKeyFocusesOnValueAtKey() {
        Lens<Map<String, Integer>, Map<String, Integer>, Integer, Integer> atFoo = UnsafeMapLens.atKey("foo");

        assertEquals((Integer) 1, view(atFoo, m));
        assertEquals((Integer) (-1), view(atFoo, set(atFoo, -1, m)));
        assertEquals((Integer) (-2), view(atFoo, over(atFoo, x -> x * 2, m)));
    }

    @Test
    public void keysFocusOnKeys() {
        Lens<Map<String, Integer>, Map<String, Integer>, Set<String>, Set<String>> keys = keys();

        assertEquals(m.keySet(), view(keys, m));
        assertEquals(new HashMap<String, Integer>() {{
            put("bar", 2);
            put("baz", 3);
            put("quux", null);
        }}, set(keys, new HashSet<>(asList("bar", "baz", "quux")), m));
    }

    @Test
    public void valuesFocusOnValues() {
        Lens<Map<String, Integer>, Map<String, Integer>, Collection<Integer>, Fn2<String, Integer, Integer>> values = UnsafeMapLens.values();

        assertEquals(m.values(), view(values, m));
        assertEquals(new HashMap<String, Integer>() {{
                         put("foo", 4);
                         put("bar", 5);
                         put("baz", 6);
                     }},
                     set(values, (k, v) -> k.length() + v, m));
    }

    @Test
    public void invertFlipsKeysAndValues() {
        Lens.Simple<Map<String, Integer>, Map<Integer, String>> invert = UnsafeMapLens.invert();

        assertEquals(new HashMap<Integer, String>() {{
            put(1, "foo");
            put(2, "bar");
            put(3, "baz");
        }}, view(invert, m));

        assertEquals(new HashMap<String, Integer>() {{
            put("bar", 2);
            put("baz", 3);
        }}, set(invert, new HashMap<Integer, String>() {{
            put(2, "bar");
            put(3, "baz");
        }}, m));
    }
}