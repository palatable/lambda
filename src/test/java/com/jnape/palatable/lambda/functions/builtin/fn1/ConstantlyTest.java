package com.jnape.palatable.lambda.functions.builtin.fn1;

import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConstantlyTest {

    @Test
    public void returnsConstantOutputRegardlessOfInput() {
        Object anything = "doesn't matter";
        assertThat(constantly("yes").apply(anything), is("yes"));
    }
}
