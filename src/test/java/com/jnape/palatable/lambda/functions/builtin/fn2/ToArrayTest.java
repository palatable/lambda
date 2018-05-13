package com.jnape.palatable.lambda.functions.builtin.fn2;

import org.junit.Test;

import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static com.jnape.palatable.lambda.functions.builtin.fn2.ToArray.toArray;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyIterator;
import static org.junit.Assert.assertArrayEquals;

public class ToArrayTest {

    @Test
    public void writesIterableToArray() {
        assertArrayEquals(new Integer[]{1, 2, 3}, toArray(Integer[].class, asList(1, 2, 3)));

        List<? extends Integer> variance = asList(1, 2, 3);
        assertArrayEquals(new Object[]{1, 2, 3}, toArray(Object[].class, variance));
    }

    @Test
    public void usesCollectionToArrayIfPossible() {
        Object sentinel = new Object();
        class CustomCollection extends AbstractCollection<Object> {
            @Override
            public Iterator<Object> iterator() {
                return emptyIterator();
            }

            @Override
            public int size() {
                return 0;
            }

            @Override
            @SuppressWarnings("unchecked")
            public <T> T[] toArray(T[] a) {
                T[] result = Arrays.copyOf(a, 1);
                result[0] = (T) sentinel;
                return result;
            }
        }

        assertArrayEquals(new Object[]{sentinel}, toArray(Object[].class, new CustomCollection()));
    }
}