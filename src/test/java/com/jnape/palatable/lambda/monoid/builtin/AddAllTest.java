package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.monoid.Monoid;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static com.jnape.palatable.lambda.monoid.builtin.AddAll.addAll;
import static java.util.Collections.singleton;
import static org.junit.Assert.assertEquals;

public class AddAllTest {

    @Test
    @SuppressWarnings("serial")
    public void monoid() {
        Monoid<Set<Integer>> addAll = addAll(HashSet::new);

        assertEquals(new HashSet<>(), addAll.identity());
        assertEquals(new HashSet<Integer>() {{
            add(1);
            add(2);
        }}, addAll.apply(new HashSet<>(singleton(1)), new HashSet<>(singleton(2))));
    }
}