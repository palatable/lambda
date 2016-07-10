package com.jnape.palatable.lambda.functions.builtin.monadic;

import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.monadic.Id.id;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class IdTest {

    @Test
    public void returnsInput() {
        assertThat(id().apply("anything"), is("anything"));
    }
}
