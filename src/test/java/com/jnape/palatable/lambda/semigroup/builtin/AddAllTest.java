package com.jnape.palatable.lambda.semigroup.builtin;

import org.junit.Test;

import java.util.HashSet;

import static com.jnape.palatable.lambda.semigroup.builtin.AddAll.addAll;
import static java.util.Collections.singleton;
import static org.junit.Assert.assertEquals;

public class AddAllTest {

    @Test
    public void semigroup() {
        assertEquals(new HashSet<Integer>() {{
            add(1);
            add(2);
        }}, addAll(new HashSet<>(singleton(1)), new HashSet<>(singleton(2))));
    }
}