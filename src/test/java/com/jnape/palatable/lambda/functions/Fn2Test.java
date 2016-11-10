package com.jnape.palatable.lambda.functions;

import org.junit.Test;

import java.util.function.BiFunction;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class Fn2Test {

    private static final Fn2<String, Integer, Boolean> CHECK_LENGTH
            = (string, length) -> string.length() == length;

    @Test
    public void flipSwapsArguments() {
        assertThat(CHECK_LENGTH.flip().apply(3, "foo"), is(true));
    }

    @Test
    public void canBePartiallyApplied() {
        assertThat(CHECK_LENGTH.apply("quux").apply(4), is(true));
    }

    @Test
    public void uncurries() {
        assertThat(CHECK_LENGTH.uncurry().apply(tuple("abc", 3)), is(true));
    }

    @Test
    public void toBiFunction() {
        BiFunction<String, Integer, Boolean> biFunction = CHECK_LENGTH.toBiFunction();
        assertEquals(true, biFunction.apply("abc", 3));
    }

    @Test
    public void adapt() {
        BiFunction<String, String, String> format = String::format;
        assertEquals("foo bar", Fn2.adapt(format).apply("foo %s", "bar"));
    }
}
