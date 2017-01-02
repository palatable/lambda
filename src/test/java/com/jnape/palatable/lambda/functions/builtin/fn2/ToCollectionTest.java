package com.jnape.palatable.lambda.functions.builtin.fn2;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

import static com.jnape.palatable.lambda.functions.builtin.fn2.ToCollection.toCollection;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class ToCollectionTest {

    @Test
    public void convertsIterablesToCollectionInstance() {
        Supplier<Collection<Integer>> listFactory = ArrayList::new;
        assertEquals(asList(1, 2, 3), toCollection(listFactory, asList(1, 2, 3)));
    }
}