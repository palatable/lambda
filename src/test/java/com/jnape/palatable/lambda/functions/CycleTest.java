package com.jnape.palatable.lambda.functions;

import org.junit.Test;

import static com.jnape.palatable.lambda.functions.Cycle.cycle;
import static com.jnape.palatable.lambda.functions.Take.take;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

public class CycleTest {

    @Test
    public void cyclesTheSameSequenceForever() {
        assertThat(take(9, cycle(1, 2, 3)), iterates(1, 2, 3, 1, 2, 3, 1, 2, 3));
    }
}
