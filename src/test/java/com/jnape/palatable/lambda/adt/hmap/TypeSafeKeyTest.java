package com.jnape.palatable.lambda.adt.hmap;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.hmap.HMap.emptyHMap;
import static com.jnape.palatable.lambda.adt.hmap.TypeSafeKey.typeSafeKey;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.lens.Iso.simpleIso;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static testsupport.assertion.LensAssert.assertLensLawfulness;


public class TypeSafeKeyTest {

    @Test
    public void lensLawfulness() {
        assertLensLawfulness(TypeSafeKey.<String>typeSafeKey().andThen(simpleIso(Integer::parseInt, Object::toString)),
                             asList("123", "0"),
                             asList(456, -1));
    }

    @Test
    public void compositionMapsOriginalValueInAndOutOfHMap() {
        TypeSafeKey.Simple<String> stringKey = typeSafeKey();
        TypeSafeKey<String, Integer> intKey = stringKey.andThen(simpleIso(Integer::parseInt, Object::toString));
        HMap map = emptyHMap().put(stringKey, "123");

        assertEquals(just("123"), map.get(stringKey));
        assertEquals(just(123), map.get(intKey));

        HMap updated = map.put(intKey, 456);
        assertEquals(just("456"), updated.get(stringKey));
        assertEquals(just(456), updated.get(intKey));

        assertEquals(1, updated.keys().size());
    }

    @Test
    public void discardRPreservesTypeSafeKey() {
        TypeSafeKey.Simple<String> stringKey = typeSafeKey();
        TypeSafeKey<String, String> discardedKey = stringKey.discardR(simpleIso(id(), id()));
        HMap map = emptyHMap().put(stringKey, "123");

        assertEquals(just("123"), map.get(discardedKey));
    }
}