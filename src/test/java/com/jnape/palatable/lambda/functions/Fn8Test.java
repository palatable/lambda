package com.jnape.palatable.lambda.functions;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.Fn8.fn8;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;

public class Fn8Test {

    private static final Fn8<String, String, String, String, String, String, String, String, String> APPEND =
            (s1, s2, s3, s4, s5, s6, s7, s8) -> s1 + s2 + s3 + s4 + s5 + s6 + s7 + s8;

    @Test
    public void canBePartiallyApplied() {
        assertThat(APPEND.apply("a").apply("b").apply("c").apply("d").apply("e").apply("f").apply("g").apply("h"), is("abcdefgh"));
        assertThat(APPEND.apply("a").apply("b").apply("c").apply("d").apply("e").apply("f").apply("g", "h"), is("abcdefgh"));
        assertThat(APPEND.apply("a").apply("b").apply("c").apply("d").apply("e").apply("f", "g", "h"), is("abcdefgh"));
        assertThat(APPEND.apply("a").apply("b").apply("c").apply("d").apply("e", "f", "g", "h"), is("abcdefgh"));
        assertThat(APPEND.apply("a").apply("b").apply("c").apply("d", "e", "f", "g", "h"), is("abcdefgh"));
        assertThat(APPEND.apply("a").apply("b").apply("c", "d", "e", "f", "g", "h"), is("abcdefgh"));
        assertThat(APPEND.apply("a").apply("b", "c", "d", "e", "f", "g", "h"), is("abcdefgh"));
        assertThat(APPEND.apply("a", "b", "c", "d", "e", "f", "g", "h"), is("abcdefgh"));
    }

    @Test
    public void flipsFirstAndSecondArgument() {
        assertThat(APPEND.flip().apply("a", "b", "c", "d", "e", "f", "g", "h"), is("bacdefgh"));
    }

    @Test
    public void uncurries() {
        assertThat(APPEND.uncurry().apply(tuple("a", "b"), "c", "d", "e", "f", "g", "h"), is("abcdefgh"));
    }

    @Test
    public void staticFactoryMethods() {
        Fn1<String, Fn7<String, String, String, String, String, String, String, String>> fn1 = a -> (b, c, d, e, f, g, h) -> a + b + c + d + e + f + g + h;
        assertEquals("abcdefgh", fn8(fn1).apply("a", "b", "c", "d", "e", "f", "g", "h"));

        Fn2<String, String, Fn6<String, String, String, String, String, String, String>> fn2 = (a, b) -> (c, d, e, f, g, h) -> a + b + c + d + e + f + g + h;
        assertEquals("abcdefgh", fn8(fn2).apply("a", "b", "c", "d", "e", "f", "g", "h"));

        Fn3<String, String, String, Fn5<String, String, String, String, String, String>> fn3 = (a, b, c) -> (d, e, f, g, h) -> a + b + c + d + e + f + g + h;
        assertEquals("abcdefgh", fn8(fn3).apply("a", "b", "c", "d", "e", "f", "g", "h"));

        Fn4<String, String, String, String, Fn4<String, String, String, String, String>> fn4 = (a, b, c, d) -> (e, f, g, h) -> a + b + c + d + e + f + g + h;
        assertEquals("abcdefgh", fn8(fn4).apply("a", "b", "c", "d", "e", "f", "g", "h"));

        Fn5<String, String, String, String, String, Fn3<String, String, String, String>> fn5 = (a, b, c, d, e) -> (f, g, h) -> a + b + c + d + e + f + g + h;
        assertEquals("abcdefgh", fn8(fn5).apply("a", "b", "c", "d", "e", "f", "g", "h"));

        Fn6<String, String, String, String, String, String, Fn2<String, String, String>> fn6 = (a, b, c, d, e, f) -> (g, h) -> a + b + c + d + e + f + g + h;
        assertEquals("abcdefgh", fn8(fn6).apply("a", "b", "c", "d", "e", "f", "g", "h"));

        Fn7<String, String, String, String, String, String, String, Fn1<String, String>> fn7 = (a, b, c, d, e, f, g) -> (h) -> a + b + c + d + e + f + g + h;
        assertEquals("abcdefgh", fn8(fn7).apply("a", "b", "c", "d", "e", "f", "g", "h"));

        assertEquals("abcdefgh", Fn8.<String, String, String, String, String, String, String, String, String>fn8((a, b, c, d, e, f, g, h) -> a + b + c + d + e + f + g + h).apply("a", "b", "c", "d", "e", "f", "g", "h"));
    }
}
