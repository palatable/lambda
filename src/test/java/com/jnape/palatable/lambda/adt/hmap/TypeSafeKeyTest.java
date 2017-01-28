package com.jnape.palatable.lambda.adt.hmap;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.hmap.TypeSafeKey.typeSafeKey;
import static org.junit.Assert.assertFalse;

public class TypeSafeKeyTest {

    @Test
    public void usesReferenceEquality() {
        assertFalse(typeSafeKey().equals(typeSafeKey()));
    }
}