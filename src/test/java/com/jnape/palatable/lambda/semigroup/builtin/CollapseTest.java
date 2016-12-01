package com.jnape.palatable.lambda.semigroup.builtin;

import com.jnape.palatable.lambda.semigroup.Semigroup;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.semigroup.builtin.Collapse.collapse;
import static org.junit.Assert.assertEquals;

public class CollapseTest {

    @Test
    public void semigroup() {
        Semigroup<String> join = (x, y) -> x + y;
        Semigroup<Integer> add = (x, y) -> x + y;

        Collapse<String, Integer> collapse = collapse();
        assertEquals(tuple("foobar", 3), collapse.apply(join, add, tuple("foo", 1), tuple("bar", 2)));
    }
}