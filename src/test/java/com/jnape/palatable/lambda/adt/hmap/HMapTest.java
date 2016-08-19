package com.jnape.palatable.lambda.adt.hmap;

import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.adt.hmap.HMap.emptyHMap;
import static com.jnape.palatable.lambda.adt.hmap.HMap.hMap;
import static com.jnape.palatable.lambda.adt.hmap.HMap.singletonHMap;
import static com.jnape.palatable.lambda.adt.hmap.TypeSafeKey.typeSafeKey;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static testsupport.matchers.IterableMatcher.iterates;

public class HMapTest {

    @Test
    public void getForPresentKey() {
        TypeSafeKey<String> stringKey = typeSafeKey();
        assertEquals(Optional.of("string value"),
                     singletonHMap(stringKey, "string value").get(stringKey));
    }

    @Test
    public void getForAbsentKey() {
        assertEquals(Optional.empty(),
                     singletonHMap(typeSafeKey(), "string value")
                             .get(typeSafeKey()));
    }

    @Test
    public void getForPresentKeyWithNullValue() {
        TypeSafeKey<String> stringKey = typeSafeKey();
        assertEquals(Optional.empty(),
                     singletonHMap(stringKey, null).get(stringKey));
    }

    @Test
    public void put() {
        TypeSafeKey<String> stringKey = typeSafeKey();
        assertEquals(singletonHMap(stringKey, "string value"),
                     emptyHMap().put(stringKey, "string value"));

        assertEquals(singletonHMap(stringKey, "new value"),
                     emptyHMap()
                             .put(stringKey, "string value")
                             .put(stringKey, "new value"));
    }

    @Test
    public void putAll() {
        TypeSafeKey<String> stringKey1 = typeSafeKey();
        TypeSafeKey<String> stringKey2 = typeSafeKey();
        TypeSafeKey<Integer> intKey = typeSafeKey();

        HMap left = hMap(stringKey1, "string value",
                         intKey, 1);
        HMap right = hMap(stringKey2, "another string value",
                          intKey, 2);

        assertEquals(hMap(stringKey1, "string value",
                          stringKey2, "another string value",
                          intKey, 2),
                     left.putAll(right));
        assertEquals(hMap(stringKey1, "string value",
                          stringKey2, "another string value",
                          intKey, 1),
                     right.putAll(left));
    }

    @Test
    public void remove() {
        TypeSafeKey<String> stringKey1 = typeSafeKey();
        TypeSafeKey<String> stringKey2 = typeSafeKey();
        assertEquals(emptyHMap(),
                     emptyHMap()
                             .put(stringKey1, "string value")
                             .remove(stringKey1));

        assertEquals(singletonHMap(stringKey2, "another string value"),
                     emptyHMap()
                             .put(stringKey1, "string value")
                             .put(stringKey2, "another string value")
                             .remove(stringKey1));
    }

    @Test
    public void removeAll() {
        TypeSafeKey<String> stringKey1 = typeSafeKey();
        TypeSafeKey<String> stringKey2 = typeSafeKey();

        HMap hMap1 = hMap(stringKey1, "foo",
                          stringKey2, "bar");
        HMap hMap2 = singletonHMap(stringKey1, "foo");

        assertEquals(singletonHMap(stringKey2, "bar"),
                     hMap1.removeAll(hMap2));
    }

    @Test
    public void containsKey() {
        TypeSafeKey<String> stringKey1 = typeSafeKey();
        TypeSafeKey<String> stringKey2 = typeSafeKey();
        TypeSafeKey<Integer> intKey = typeSafeKey();

        HMap hMap = singletonHMap(stringKey1, "string");

        assertTrue(hMap.containsKey(stringKey1));
        assertFalse(hMap.containsKey(stringKey2));
        assertFalse(hMap.containsKey(intKey));
    }

    @Test
    public void demandForPresentKey() {
        TypeSafeKey<String> stringKey = typeSafeKey();
        assertEquals("string value",
                     singletonHMap(stringKey, "string value").demand(stringKey));
    }

    @Test(expected = NoSuchElementException.class)
    public void demandForAbsentKey() {
        emptyHMap().demand(typeSafeKey());
    }

    @Test
    public void iteratesKVPairsAsTuples() {
        TypeSafeKey<String> stringKey = typeSafeKey();

        assertThat(singletonHMap(stringKey, "string value"),
                   iterates(tuple(stringKey, "string value")));
    }

    @Test
    public void keys() {
        TypeSafeKey<String> stringKey = typeSafeKey();

        assertThat(singletonHMap(stringKey, "string value").keys(),
                   iterates(stringKey));
    }

    @Test
    public void values() {
        assertThat(singletonHMap(typeSafeKey(), "string value").values(),
                   iterates("string value"));
    }

    @Test
    public void convenienceStaticFactoryMethods() {
        TypeSafeKey<String> stringKey = typeSafeKey();
        TypeSafeKey<Integer> intKey = typeSafeKey();
        TypeSafeKey<Float> floatKey = typeSafeKey();
        assertEquals(emptyHMap().put(stringKey, "string value"),
                     singletonHMap(stringKey, "string value"));
        assertEquals(emptyHMap().put(stringKey, "string value").put(intKey, 1),
                     hMap(stringKey, "string value",
                          intKey, 1));
        assertEquals(emptyHMap().put(stringKey, "string value").put(intKey, 1).put(floatKey, 1f),
                     hMap(stringKey, "string value",
                          intKey, 1,
                          floatKey, 1f));
    }

    @Test
    @SuppressWarnings("EqualsWithItself")
    public void equality() {
        assertTrue(emptyHMap().equals(emptyHMap()));

        TypeSafeKey<String> stringKey = typeSafeKey();
        assertTrue(emptyHMap().put(stringKey, "one").equals(emptyHMap().put(stringKey, "one")));

        assertFalse(emptyHMap().equals(emptyHMap().put(stringKey, "string key")));
        assertFalse(emptyHMap().put(stringKey, "string key").equals(emptyHMap()));
        assertFalse(emptyHMap().put(typeSafeKey(), "one").equals(emptyHMap().put(typeSafeKey(), "one")));
        assertFalse(emptyHMap().put(typeSafeKey(), "one").equals(emptyHMap().put(typeSafeKey(), 1)));
        assertFalse(emptyHMap().put(typeSafeKey(), 1).equals(emptyHMap().put(typeSafeKey(), "one")));
    }

    @Test
    public void hashCodeUsesDecentDistribution() {
        assertEquals(emptyHMap().hashCode(), emptyHMap().hashCode());
        TypeSafeKey<String> stringKey = typeSafeKey();
        assertEquals(singletonHMap(stringKey, "string value").hashCode(),
                     singletonHMap(stringKey, "string value").hashCode());

        assertNotEquals(emptyHMap(), singletonHMap(stringKey, "string value"));
        assertNotEquals(singletonHMap(stringKey, "string value"),
                        singletonHMap(stringKey, "another string value"));
    }

    @Test
    public void emptyHMapReusesInstance() {
        assertSame(emptyHMap(), emptyHMap());
    }
}