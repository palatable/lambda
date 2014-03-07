package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.ImmutableIteration;
import testsupport.traits.InfiniteIteration;
import testsupport.traits.Laziness;

import static com.jnape.palatable.lambda.functions.Cycle.cycle;
import static com.jnape.palatable.lambda.functions.Take.take;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class CycleTest {

    @TestTraits({Laziness.class, ImmutableIteration.class, InfiniteIteration.class})
    public Cycle createTestSubject() {
        return new Cycle();
    }

    @Test
    public void cyclesTheSameSequenceForever() {
        assertThat(take(9, cycle(1, 2, 3)), iterates(1, 2, 3, 1, 2, 3, 1, 2, 3));
    }
}
