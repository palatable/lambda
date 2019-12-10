package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.monoid.Monoid;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.monoid.builtin.Collapse.collapse;
import static com.jnape.palatable.lambda.monoid.builtin.Join.join;
import static org.junit.Assert.assertEquals;

public class CollapseTest {

    @Test
    public void monoid() {
        Monoid<String>  join = join();
        Monoid<Integer> add  = Monoid.monoid(Integer::sum, 0);

        Collapse<String, Integer> collapse = collapse();

        assertEquals(tuple("", 0), collapse.apply(join, add).identity());
        assertEquals(tuple("foobar", 3), collapse.apply(join, add, tuple("foo", 1), tuple("bar", 2)));
    }
}