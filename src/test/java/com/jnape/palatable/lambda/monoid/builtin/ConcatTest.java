package com.jnape.palatable.lambda.monoid.builtin;

import org.junit.Test;

import static com.jnape.palatable.lambda.monoid.builtin.Concat.concat;
import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.isEmpty;
import static testsupport.matchers.IterableMatcher.iterates;

public class ConcatTest {

    @Test
    public void monoid() {
        assertThat(concat().identity(), isEmpty());
        assertThat(concat(asList(1, 2), singleton(3)), iterates(1, 2, 3));
    }
}