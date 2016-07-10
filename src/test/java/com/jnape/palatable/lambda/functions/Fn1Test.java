package com.jnape.palatable.lambda.functions;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Fn1Test {

    @Test
    public void fmapComposesFunctions() {
        Fn1<Integer, Integer> add2 = integer -> integer + 2;
        Fn1<Integer, String> toString = Object::toString;

        assertThat(add2.fmap(toString).apply(2), is(toString.apply(add2.apply(2))));
    }

    @Test
    public void thenIsJustAnAliasForFmap() {
        Fn1<Integer, Integer> add2 = integer -> integer + 2;
        Fn1<Integer, String> toString = Object::toString;

        assertThat(add2.then(toString).apply(2), is(toString.apply(add2.apply(2))));
    }
}
