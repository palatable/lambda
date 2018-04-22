package com.jnape.palatable.lambda.adt.hmap;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.hmap.TypeSafeKey.typeSafeKey;
import static org.junit.Assert.assertNotEquals;

public class SimpleTest {

    @Test
    public void usesReferenceEquality() {
        assertNotEquals(typeSafeKey(), typeSafeKey());
    }
}