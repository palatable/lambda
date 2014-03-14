package com.jnape.palatable.lambda.functions.builtin.monadic;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class IdentityTest {

    @Test
    public void returnsInput() {
        assertThat(new Identity<String>().apply("anything"), is("anything"));
    }
}
