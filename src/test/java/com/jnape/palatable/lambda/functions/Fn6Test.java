package com.jnape.palatable.lambda.functions;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class Fn6Test {

    private static final Fn6<String, String, String, String, String, String, String> APPEND =
            (s1, s2, s3, s4, s5, s6) -> s1 + s2 + s3 + s4 + s5 + s6;

    @Test
    public void canBePartiallyApplied() {
        assertThat(APPEND.apply("a").apply("b").apply("c").apply("d").apply("e").apply("f"), is("abcdef"));
        assertThat(APPEND.apply("a").apply("b").apply("c").apply("d").apply("e", "f"), is("abcdef"));
        assertThat(APPEND.apply("a").apply("b").apply("c").apply("d", "e", "f"), is("abcdef"));
        assertThat(APPEND.apply("a").apply("b").apply("c", "d", "e", "f"), is("abcdef"));
        assertThat(APPEND.apply("a").apply("b", "c", "d", "e", "f"), is("abcdef"));
        assertThat(APPEND.apply("a", "b", "c", "d", "e", "f"), is("abcdef"));
    }

    @Test
    public void flipsFirstAndSecondArgument() {
        assertThat(APPEND.flip().apply("a", "b", "c", "d", "e", "f"), is("bacdef"));
    }

    @Test
    public void uncurries() {
        assertThat(APPEND.uncurry().apply(tuple("a", "b"), "c", "d", "e", "f"), is("abcdef"));
    }

    @Test
    public void staticFactoryMethods() {
        Fn1<String, Fn5<String, String, String, String, String, String>> fn1 = a -> (b, c, d, e, f) -> a + b + c + d + e + f;
        assertEquals("abcdef", Fn6.fn6(fn1).apply("a", "b", "c", "d", "e", "f"));

        Fn2<String, String, Fn4<String, String, String, String, String>> fn2 = (a, b) -> (c, d, e, f) -> a + b + c + d + e + f;
        assertEquals("abcdef", Fn6.fn6(fn2).apply("a", "b", "c", "d", "e", "f"));

        Fn3<String, String, String, Fn3<String, String, String, String>> fn3 = (a, b, c) -> (d, e, f) -> a + b + c + d + e + f;
        assertEquals("abcdef", Fn6.fn6(fn3).apply("a", "b", "c", "d", "e", "f"));

        Fn4<String, String, String, String, Fn2<String, String, String>> fn4 = (a, b, c, d) -> (e, f) -> a + b + c + d + e + f;
        assertEquals("abcdef", Fn6.fn6(fn4).apply("a", "b", "c", "d", "e", "f"));

        Fn5<String, String, String, String, String, Fn1<String, String>> fn5 = (a, b, c, d, e) -> (f) -> a + b + c + d + e + f;
        assertEquals("abcdef", Fn6.fn6(fn5).apply("a", "b", "c", "d", "e", "f"));

        assertEquals("abcdef", Fn6.<String, String, String, String, String, String, String>fn6((a, b, c, d, e, f) -> a + b + c + d + e + f).apply("a", "b", "c", "d", "e", "f"));
    }
}
