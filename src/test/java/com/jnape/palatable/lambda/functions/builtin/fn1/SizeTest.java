package com.jnape.palatable.lambda.functions.builtin.fn1;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Size.size;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;

public class SizeTest {

    @Test
    public void countsElementsInIterable() {
        assertEquals((Long) 0L, size(emptyList()));
        assertEquals((Long) 3L, size(asList(1, 2, 3)));
    }

    @Test
    @SuppressWarnings("serial")
    public void optimizesForCollections() {
        Collection<Integer> collection = new ArrayList<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                throw new IllegalStateException("should not be using the iterator");
            }

            {
                add(1);
                add(2);
                add(3);
            }
        };
        assertEquals((Long) 3L, size(collection));
    }
}