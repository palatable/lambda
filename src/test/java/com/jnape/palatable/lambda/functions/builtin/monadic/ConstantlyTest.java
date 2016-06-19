package com.jnape.palatable.lambda.functions.builtin.monadic;

import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.monadic.Constantly.constantly;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ConstantlyTest {

    @Test
    public void returnsConstantOutputRegardlessOfInput() {
        Object anything = "doesn't matter";
        assertThat(constantly("yes").apply(anything), is("yes"));
    }
}
