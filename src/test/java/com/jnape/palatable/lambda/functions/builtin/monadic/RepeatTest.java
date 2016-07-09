package com.jnape.palatable.lambda.functions.builtin.monadic;

import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.ImmutableIteration;
import testsupport.traits.InfiniteIteration;
import testsupport.traits.Laziness;

import static com.jnape.palatable.lambda.functions.builtin.dyadic.Take.take;
import static com.jnape.palatable.lambda.functions.builtin.monadic.Repeat.repeat;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class RepeatTest {

    @TestTraits({Laziness.class, ImmutableIteration.class, InfiniteIteration.class})
    public Repeat createTestSubject() {
        return repeat();
    }

    @Test
    public void repeatsTheSameValueForever() {
        assertThat(take(10, repeat(1)), iterates(1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    }
}
