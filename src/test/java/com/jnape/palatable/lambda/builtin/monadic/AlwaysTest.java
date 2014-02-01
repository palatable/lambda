package com.jnape.palatable.lambda.builtin.monadic;

import org.junit.Test;

import static com.jnape.palatable.lambda.builtin.monadic.Always.always;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AlwaysTest {

    @Test
    public void returnsConstantOutputRegardlessOfInput() {
        Object anything = "doesn't matter";
        assertThat(always("yes").apply(anything), is("yes"));
    }
}
