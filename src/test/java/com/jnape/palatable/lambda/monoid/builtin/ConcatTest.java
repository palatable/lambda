package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.monoid.Monoid;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static com.jnape.palatable.lambda.monoid.builtin.Concat.concat;
import static java.util.Collections.singleton;
import static org.junit.Assert.assertEquals;

public class ConcatTest {

    @Test
    public void monoid() {
        Monoid<Set<Integer>> concat = concat(HashSet::new);

        assertEquals(new HashSet<>(), concat.identity());
        assertEquals(new HashSet<Integer>() {{
            add(1);
            add(2);
        }}, concat.apply(new HashSet<>(singleton(1)), new HashSet<>(singleton(2))));
    }
}