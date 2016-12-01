package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.adt.hmap.HMap;
import com.jnape.palatable.lambda.adt.hmap.TypeSafeKey;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.hmap.HMap.emptyHMap;
import static com.jnape.palatable.lambda.adt.hmap.HMap.singletonHMap;
import static com.jnape.palatable.lambda.adt.hmap.TypeSafeKey.typeSafeKey;
import static com.jnape.palatable.lambda.monoid.builtin.PutAll.putAll;
import static org.junit.Assert.assertEquals;

public class PutAllTest {

    @Test
    public void identity() {
        assertEquals(emptyHMap(), putAll().identity());
    }

    @Test
    public void monoid() {
        TypeSafeKey<String> stringKey = typeSafeKey();
        TypeSafeKey<Integer> intKey = typeSafeKey();

        HMap x = singletonHMap(stringKey, "string");
        HMap y = singletonHMap(intKey, 1);

        HMap result = putAll(x, y);
        assertEquals("string", result.demand(stringKey));
        assertEquals((Integer) 1, result.demand(intKey));
    }
}