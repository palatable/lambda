package com.jnape.palatable.lambda.functions;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.Fn3.fn3;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;

public class Fn3Test {

    private static final Fn3<Integer, Integer, Integer, Boolean> CHECK_MULTIPLICATION =
            (multiplicand, multiplier, guessResult) -> multiplicand * multiplier == guessResult;

    @Test
    public void canBePartiallyApplied() {
        assertThat(CHECK_MULTIPLICATION.apply(2).apply(4).apply(8), is(true));
        assertThat(CHECK_MULTIPLICATION.apply(2).apply(4, 8), is(true));
        assertThat(CHECK_MULTIPLICATION.apply(2, 4).apply(8), is(true));
    }

    @Test
    public void flipsFirstAndSecondArgument() {
        Fn3<String, Integer, String, String> concat3 = (a, b, c) -> a + b.toString() + c;
        assertThat(concat3.flip().apply(1, "2", "3"), is("213"));
    }

    @Test
    public void uncurries() {
        assertThat(CHECK_MULTIPLICATION.uncurry().apply(tuple(2, 3), 6), is(true));
    }

    @Test
    public void staticFactoryMethods() {
        Fn1<String, Fn2<String, String, String>> fn1 = a -> (b, c) -> a + b + c;
        assertEquals("abc", fn3(fn1).apply("a", "b", "c"));

        Fn2<String, String, Fn1<String, String>> fn2 = (a, b) -> c -> a + b + c;
        assertEquals("abc", fn3(fn2).apply("a", "b", "c"));

        assertEquals("abc", Fn3.<String, String, String, String>fn3((a, b, c) -> a + b + c).apply("a", "b", "c"));
    }
}
