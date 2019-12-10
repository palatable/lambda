package com.jnape.palatable.lambda.semigroup.builtin;

import org.junit.Test;

import static com.jnape.palatable.lambda.io.IO.io;
import static com.jnape.palatable.lambda.semigroup.builtin.RunAll.runAll;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IOMatcher.yieldsValue;

public class RunAllTest {

    @Test
    public void semigroup() {
        assertThat(runAll(Integer::sum).apply(io(1), io(2)), yieldsValue(equalTo(3)));
    }
}