package com.jnape.palatable.lambda.functions.builtin.fn2;

import org.junit.Test;

import java.util.ArrayList;

import static com.jnape.palatable.lambda.functions.builtin.fn2.ToCollection.toCollection;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class ToCollectionTest {

    @Test
    public void convertsIterablesToCollectionInstance() {
        assertEquals(asList(1, 2, 3), toCollection(ArrayList::new, asList(1, 2, 3)));
    }
}