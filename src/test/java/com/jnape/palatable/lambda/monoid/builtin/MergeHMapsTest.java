package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.adt.hmap.TypeSafeKey;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.hmap.HMap.emptyHMap;
import static com.jnape.palatable.lambda.adt.hmap.HMap.hMap;
import static com.jnape.palatable.lambda.adt.hmap.HMap.singletonHMap;
import static com.jnape.palatable.lambda.adt.hmap.TypeSafeKey.typeSafeKey;
import static com.jnape.palatable.lambda.monoid.builtin.Join.join;
import static com.jnape.palatable.lambda.monoid.builtin.MergeHMaps.mergeHMaps;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class MergeHMapsTest {

    @Test
    public void allKeysAccountedFor() {
        TypeSafeKey.Simple<String> stringKey  = typeSafeKey();
        MergeHMaps                 mergeHMaps = mergeHMaps().key(stringKey, join());

        assertEquals(emptyHMap(), mergeHMaps.apply(emptyHMap(), emptyHMap()));
        assertEquals(singletonHMap(stringKey, "foo"),
                     mergeHMaps.apply(singletonHMap(stringKey, "foo"), emptyHMap()));
        assertEquals(singletonHMap(stringKey, "foobar"),
                     mergeHMaps.apply(singletonHMap(stringKey, "foo"),
                                      singletonHMap(stringKey, "bar")));
    }

    @Test
    public void unaccountedForKeyUsesLastByDefault() {
        TypeSafeKey.Simple<String> stringKey = typeSafeKey();

        assertEquals(singletonHMap(stringKey, "foo"),
                     mergeHMaps().apply(singletonHMap(stringKey, "foo"), emptyHMap()));
        assertEquals(singletonHMap(stringKey, "bar"),
                     mergeHMaps().apply(emptyHMap(), singletonHMap(stringKey, "bar")));
        assertEquals(singletonHMap(stringKey, "bar"),
                     mergeHMaps().apply(singletonHMap(stringKey, "foo"), singletonHMap(stringKey, "bar")));
    }

    @Test
    public void sparseKeysAcrossMaps() {
        TypeSafeKey.Simple<String>  stringKey = typeSafeKey();
        TypeSafeKey.Simple<Integer> intKey    = typeSafeKey();
        TypeSafeKey.Simple<Boolean> boolKey   = typeSafeKey();

        MergeHMaps mergeHMaps = mergeHMaps()
                .key(stringKey, join())
                .key(intKey, Integer::sum);

        assertEquals(hMap(stringKey, "foobar",
                          intKey, 3,
                          boolKey, false),
                     mergeHMaps.reduceLeft(asList(singletonHMap(stringKey, "foo"),
                                                  singletonHMap(intKey, 1),
                                                  singletonHMap(boolKey, true),
                                                  hMap(stringKey, "bar",
                                                       intKey, 2,
                                                       boolKey, false))));
    }
}