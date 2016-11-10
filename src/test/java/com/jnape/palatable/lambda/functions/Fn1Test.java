package com.jnape.palatable.lambda.functions;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

import java.util.function.Function;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;

public class Fn1Test {

    @Test
    public void functorProperties() {
        Fn1<Integer, Integer> add2 = integer -> integer + 2;
        Fn1<Integer, String> toString = Object::toString;

        MatcherAssert.assertThat(add2.fmap(toString).apply(2), is(toString.apply(add2.apply(2))));
    }

    @Test
    public void profunctorProperties() {
        Fn1<Integer, Integer> add2 = integer -> integer + 2;

        assertEquals((Integer) 3, add2.<String>diMapL(Integer::parseInt).apply("1"));
        assertEquals("3", add2.diMapR(Object::toString).apply(1));
        assertEquals("3", add2.<String, String>diMap(Integer::parseInt, Object::toString).apply("1"));
    }

    @Test
    public void thenIsJustAnAliasForFmap() {
        Fn1<Integer, Integer> add2 = integer -> integer + 2;
        Fn1<Integer, String> toString = Object::toString;

        MatcherAssert.assertThat(add2.then(toString).apply(2), is(toString.apply(add2.apply(2))));
    }

    @Test
    public void adapt() {
        Function<String, Integer> parseInt = Integer::parseInt;
        assertEquals((Integer) 1, Fn1.adapt(parseInt).apply("1"));
    }
}
