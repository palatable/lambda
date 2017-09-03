package com.jnape.palatable.lambda.functions.builtin.fn2;

import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Replicate.replicate;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.isEmpty;
import static testsupport.matchers.IterableMatcher.iterates;

public class ReplicateTest {

    @Test
    public void replicate0TimesProducesEmptyIterable() {
        assertThat(replicate(0, 1), isEmpty());
    }

    @Test
    public void replicateMoreThan0TimesProducesPopulatedIterable() {
        assertThat(replicate(3, '1'), iterates('1', '1', '1'));
    }
}