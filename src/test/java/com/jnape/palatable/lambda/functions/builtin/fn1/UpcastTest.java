package com.jnape.palatable.lambda.functions.builtin.fn1;

import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Upcast.upcast;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;
import static java.util.Arrays.asList;

public class UpcastTest {

    @Test
    @SuppressWarnings("unused")
    public void castsUp() {
        Upcast<String, CharSequence> upcast = upcast();
        Iterable<String> strings = asList("foo", "bar");
        Iterable<CharSequence> charSequences = map(upcast, strings);
    }
}