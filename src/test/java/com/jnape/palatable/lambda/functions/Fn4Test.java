package com.jnape.palatable.lambda.functions;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.Fn4.fn4;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;

public class Fn4Test {

    private static final Fn4<String, String, String, String, String> APPEND =
            (s1, s2, s3, s4) -> s1 + s2 + s3 + s4;

    @Test
    public void canBePartiallyApplied() {
        assertThat(APPEND.apply("a").apply("b").apply("c").apply("d"), is("abcd"));
        assertThat(APPEND.apply("a").apply("b").apply("c", "d"), is("abcd"));
        assertThat(APPEND.apply("a").apply("b", "c", "d"), is("abcd"));
        assertThat(APPEND.apply("a", "b", "c", "d"), is("abcd"));
    }

    @Test
    public void flipsFirstAndSecondArgument() {
        assertThat(APPEND.flip().apply("a", "b", "c", "d"), is("bacd"));
    }

    @Test
    public void uncurries() {
        assertThat(APPEND.uncurry().apply(tuple("a", "b"), "c", "d"), is("abcd"));
    }

    @Test
    public void staticFactoryMethods() {
        Fn1<String, Fn3<String, String, String, String>> fn1 = a -> (b, c, d) -> a + b + c + d;
        assertEquals("abcd", fn4(fn1).apply("a", "b", "c", "d"));

        Fn2<String, String, Fn2<String, String, String>> fn2 = (a, b) -> (c, d) -> a + b + c + d;
        assertEquals("abcd", fn4(fn2).apply("a", "b", "c", "d"));

        Fn3<String, String, String, Fn1<String, String>> fn3 = (a, b, c) -> (d) -> a + b + c + d;
        assertEquals("abcd", fn4(fn3).apply("a", "b", "c", "d"));

        assertEquals("abcd", Fn4.<String, String, String, String, String>fn4((a, b, c, d) -> a + b + c + d).apply("a", "b", "c", "d"));
    }
}
