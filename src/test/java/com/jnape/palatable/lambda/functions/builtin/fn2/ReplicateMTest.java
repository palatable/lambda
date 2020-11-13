package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functor.builtin.Identity;
import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn2.ReplicateM.replicateM;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterateTMatcher.isEmpty;
import static testsupport.matchers.IterateTMatcher.iterates;

public class ReplicateMTest {

    @Test
    public void emptyWithZeroReplicas() {
        assertThat(replicateM(0, new Identity<>(1)), isEmpty());
    }

    @Test
    public void populatedWithMoreThanZeroReplicas() {
        assertThat(replicateM(3, new Identity<>('1')), iterates('1', '1', '1'));
    }
}