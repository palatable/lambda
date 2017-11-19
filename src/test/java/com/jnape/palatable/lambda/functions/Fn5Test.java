package com.jnape.palatable.lambda.functions;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class Fn5Test {

    private static final Fn5<String, String, String, String, String, String> APPEND =
            (s1, s2, s3, s4, s5) -> s1 + s2 + s3 + s4 + s5;

    @Test
    public void canBePartiallyApplied() {
        assertThat(APPEND.apply("a").apply("b").apply("c").apply("d").apply("e"), is("abcde"));
        assertThat(APPEND.apply("a").apply("b").apply("c").apply("d", "e"), is("abcde"));
        assertThat(APPEND.apply("a").apply("b").apply("c", "d", "e"), is("abcde"));
        assertThat(APPEND.apply("a").apply("b", "c", "d", "e"), is("abcde"));
        assertThat(APPEND.apply("a", "b", "c", "d", "e"), is("abcde"));
    }

    @Test
    public void flipsFirstAndSecondArgument() {
        assertThat(APPEND.flip().apply("a", "b", "c", "d", "e"), is("bacde"));
    }

    @Test
    public void uncurries() {
        assertThat(APPEND.uncurry().apply(tuple("a", "b"), "c", "d", "e"), is("abcde"));
    }

    @Test
    public void fn5() {
        Fn1<String, Fn4<String, String, String, String, String>> fn1 = a -> (b, c, d, e) -> a + b + c + d + e;
        assertEquals("abcde", Fn5.fn5(fn1).apply("a", "b", "c", "d", "e"));

        Fn2<String, String, Fn3<String, String, String, String>> fn2 = (a, b) -> (c, d, e) -> a + b + c + d + e;
        assertEquals("abcde", Fn5.fn5(fn2).apply("a", "b", "c", "d", "e"));

        Fn3<String, String, String, Fn2<String, String, String>> fn3 = (a, b, c) -> (d, e) -> a + b + c + d + e;
        assertEquals("abcde", Fn5.fn5(fn3).apply("a", "b", "c", "d", "e"));

        Fn4<String, String, String, String, Fn1<String, String>> fn4 = (a, b, c, d) -> (e) -> a + b + c + d + e;
        assertEquals("abcde", Fn5.fn5(fn4).apply("a", "b", "c", "d", "e"));
    }
}
