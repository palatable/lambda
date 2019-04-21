package com.jnape.palatable.lambda.optics.lenses;

import com.jnape.palatable.lambda.adt.hmap.TypeSafeKey;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.hmap.HMap.emptyHMap;
import static com.jnape.palatable.lambda.adt.hmap.HMap.hMap;
import static com.jnape.palatable.lambda.adt.hmap.HMap.singletonHMap;
import static com.jnape.palatable.lambda.adt.hmap.TypeSafeKey.typeSafeKey;
import static java.util.Arrays.asList;
import static testsupport.assertion.LensAssert.assertLensLawfulness;

public class HMapLensTest {

    @Test
    public void valueAt() {
        TypeSafeKey.Simple<String> key = typeSafeKey();
        assertLensLawfulness(HMapLens.valueAt(key),
                             asList(emptyHMap(),
                                    singletonHMap(key, "foo"),
                                    hMap(key, "foo",
                                         typeSafeKey(), "bar"),
                                    singletonHMap(typeSafeKey(), "bar")),
                             asList(nothing(),
                                    just("foo"),
                                    just("bar")));
    }
}