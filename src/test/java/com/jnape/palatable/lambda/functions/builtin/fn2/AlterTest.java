package com.jnape.palatable.lambda.functions.builtin.fn2;

import org.junit.Test;

import java.util.ArrayList;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Alter.alter;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class AlterTest {

    @Test
    public void altersInput() {
        ArrayList<String> input = new ArrayList<>();
        assertSame(input, alter(xs -> xs.add("foo"), input));
        assertEquals(singletonList("foo"), input);
    }
}