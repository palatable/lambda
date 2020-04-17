package com.jnape.palatable.lambda.functions;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;

public class Fn7Test {

    private static final Fn7<String, String, String, String, String, String, String, String> APPEND =
            (s1, s2, s3, s4, s5, s6, s7) -> s1 + s2 + s3 + s4 + s5 + s6 + s7;

    @Test
    public void canBePartiallyApplied() {
        assertThat(APPEND.apply("a").apply("b").apply("c").apply("d").apply("e").apply("f").apply("g"), is("abcdefg"));
        assertThat(APPEND.apply("a").apply("b").apply("c").apply("d").apply("e").apply("f", "g"), is("abcdefg"));
        assertThat(APPEND.apply("a").apply("b").apply("c").apply("d").apply("e", "f", "g"), is("abcdefg"));
        assertThat(APPEND.apply("a").apply("b").apply("c").apply("d", "e", "f", "g"), is("abcdefg"));
        assertThat(APPEND.apply("a").apply("b").apply("c", "d", "e", "f", "g"), is("abcdefg"));
        assertThat(APPEND.apply("a").apply("b", "c", "d", "e", "f", "g"), is("abcdefg"));
        assertThat(APPEND.apply("a", "b", "c", "d", "e", "f", "g"), is("abcdefg"));
    }

    @Test
    public void flipsFirstAndSecondArgument() {
        assertThat(APPEND.flip().apply("a", "b", "c", "d", "e", "f", "g"), is("bacdefg"));
    }

    @Test
    public void uncurries() {
        assertThat(APPEND.uncurry().apply(tuple("a", "b"), "c", "d", "e", "f", "g"), is("abcdefg"));
    }

    @Test
    public void staticFactoryMethod() {
        Fn1<String, Fn6<String, String, String, String, String, String, String>> fn1 = a -> (b, c, d, e, f, g) -> a + b + c + d + e + f + g;
        assertEquals("abcdefg", Fn7.fn7(fn1).apply("a", "b", "c", "d", "e", "f", "g"));

        Fn2<String, String, Fn5<String, String, String, String, String, String>> fn2 = (a, b) -> (c, d, e, f, g) -> a + b + c + d + e + f + g;
        assertEquals("abcdefg", Fn7.fn7(fn2).apply("a", "b", "c", "d", "e", "f", "g"));

        Fn3<String, String, String, Fn4<String, String, String, String, String>> fn3 = (a, b, c) -> (d, e, f, g) -> a + b + c + d + e + f + g;
        assertEquals("abcdefg", Fn7.fn7(fn3).apply("a", "b", "c", "d", "e", "f", "g"));

        Fn4<String, String, String, String, Fn3<String, String, String, String>> fn4 = (a, b, c, d) -> (e, f, g) -> a + b + c + d + e + f + g;
        assertEquals("abcdefg", Fn7.fn7(fn4).apply("a", "b", "c", "d", "e", "f", "g"));

        Fn5<String, String, String, String, String, Fn2<String, String, String>> fn5 = (a, b, c, d, e) -> (f, g) -> a + b + c + d + e + f + g;
        assertEquals("abcdefg", Fn7.fn7(fn5).apply("a", "b", "c", "d", "e", "f", "g"));

        Fn6<String, String, String, String, String, String, Fn1<String, String>> fn6 = (a, b, c, d, e, f) -> (g) -> a + b + c + d + e + f + g;
        assertEquals("abcdefg", Fn7.fn7(fn6).apply("a", "b", "c", "d", "e", "f", "g"));

        assertEquals("abcdefg", Fn7.<String, String, String, String, String, String, String, String>fn7((a, b, c, d, e, f, g) -> a + b + c + d + e + f + g).apply("a", "b", "c", "d", "e", "f", "g"));
    }
}
