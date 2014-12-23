package com.jnape.palatable.lambda.functions.builtin.dyadic;

import com.jnape.palatable.lambda.functions.TriadicFunction;
import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.dyadic.Partial3.partial3;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class Partial3Test {

    @Test
    public void partiallyAppliesFunction() {
        TriadicFunction<String, String, String, String> concat = (s1, s2, s3) -> s1 + s2 + s3;

        assertThat(partial3(concat, "foo").apply(" bar", " baz"), is("foo bar baz"));
    }
}
