package com.jnape.palatable.lambda.functions.builtin.dyadic;

import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.functions.TriadicFunction;
import com.jnape.palatable.lambda.tuples.Tuple3;
import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.dyadic.Partial3.partial3;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class Partial3Test {

    @Test
    public void partiallyAppliesFunction() {
        MonadicFunction<Tuple3<String, String, String>, String> concat = new TriadicFunction<String, String, String, String>() {
            @Override
            public String apply(String s, String s2, String s3) {
                return s + s2 + s3;
            }
        };

        assertThat(partial3(concat, "foo").apply(" bar", " baz"), is("foo bar baz"));
    }
}
