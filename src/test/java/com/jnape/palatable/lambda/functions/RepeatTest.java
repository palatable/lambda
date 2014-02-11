package com.jnape.palatable.lambda.functions;

import org.junit.Test;

import static com.jnape.palatable.lambda.functions.Repeat.repeat;
import static com.jnape.palatable.lambda.functions.Take.take;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

public class RepeatTest {

    @Test
    public void repeatsTheSameValueForever() {
        assertThat(take(10, repeat(1)), iterates(1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    }
}
