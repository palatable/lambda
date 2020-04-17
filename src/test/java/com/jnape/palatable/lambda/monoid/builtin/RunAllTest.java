package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.monoid.Monoid;
import org.junit.Test;

import static com.jnape.palatable.lambda.io.IO.io;
import static com.jnape.palatable.lambda.monoid.builtin.RunAll.runAll;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static testsupport.matchers.IOMatcher.yieldsValue;

public class RunAllTest {

    @Test
    public void monoid() {
        Monoid<Integer> add = Monoid.monoid(Integer::sum, 0);
        assertThat(runAll(add).apply(io(1), io(2)), yieldsValue(equalTo(3)));
        assertThat(runAll(add).identity(), yieldsValue(equalTo(0)));
    }
}