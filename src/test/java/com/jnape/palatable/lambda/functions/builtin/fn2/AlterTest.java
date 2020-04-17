package com.jnape.palatable.lambda.functions.builtin.fn2;

import org.junit.Test;

import java.util.ArrayList;

import static com.jnape.palatable.lambda.functions.Effect.fromConsumer;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Alter.alter;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static testsupport.matchers.IOMatcher.yieldsValue;

public class AlterTest {

    @Test
    public void altersInput() {
        ArrayList<String> input = new ArrayList<>();
        assertThat(alter(fromConsumer(xs -> xs.add("foo")), input),
                   yieldsValue(allOf(sameInstance(input),
                                     equalTo(singletonList("foo")))));
    }
}