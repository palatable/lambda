package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import org.junit.Test;

import java.util.Comparator;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.SortWith.sortWith;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class SortWithTest {

    @Test
    public void sortsWithGivenComparator() {
        assertEquals(asList(tuple("bar", 4), tuple("baz", 3), tuple("foo", 1), tuple("foo", 2)),
                     sortWith(Comparator.<Tuple2<String, Integer>, String>comparing(Tuple2::_1)
                                      .thenComparing(Tuple2::_2),
                              asList(tuple("foo", 1), tuple("foo", 2), tuple("bar", 4), tuple("baz", 3))));
    }
}