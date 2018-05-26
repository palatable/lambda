package com.jnape.palatable.lambda.adt.hmap;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.adt.hmap.HMap.emptyHMap;
import static com.jnape.palatable.lambda.adt.hmap.HMap.hMap;
import static com.jnape.palatable.lambda.adt.hmap.Schema.schema;
import static com.jnape.palatable.lambda.adt.hmap.TypeSafeKey.typeSafeKey;
import static com.jnape.palatable.lambda.lens.functions.View.view;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static testsupport.assertion.LensAssert.assertLensLawfulness;

public class SchemaTest {

    @Test
    public void extractsValuesAtKeysFromMap() {
        TypeSafeKey.Simple<Byte> byteKey = typeSafeKey();
        TypeSafeKey.Simple<Short> shortKey = typeSafeKey();
        TypeSafeKey.Simple<Integer> intKey = typeSafeKey();
        TypeSafeKey.Simple<Long> longKey = typeSafeKey();
        TypeSafeKey.Simple<Float> floatKey = typeSafeKey();
        TypeSafeKey.Simple<Double> doubleKey = typeSafeKey();
        TypeSafeKey.Simple<Character> charKey = typeSafeKey();
        TypeSafeKey.Simple<Boolean> booleanKey = typeSafeKey();

        HMap m = hMap(byteKey, (byte) 1,
                      shortKey, (short) 2,
                      intKey, 3,
                      longKey, 4L,
                      floatKey, 5F,
                      doubleKey, 6D,
                      charKey, '7',
                      booleanKey, true);

        assertLensLawfulness(schema(byteKey, shortKey, intKey, longKey, floatKey, doubleKey, charKey, booleanKey),
                             asList(emptyHMap(),
                                    m),
                             asList(nothing(),
                                    just(tuple((byte) 1, (short) 2, 3, 4L, 5F, 6D, '7', true))));
    }

    @Test
    public void extractsNothingIfAnyKeysMissing() {
        assertEquals(nothing(), view(schema(typeSafeKey()), emptyHMap()));
    }
}