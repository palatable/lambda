package com.jnape.palatable.lambda.functions;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

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
    public void functorProperties() {
        assertThat(CHECK_MULTIPLICATION.fmap(f -> f.fmap(g -> g.andThen(Object::toString))).apply(2).apply(3).apply(6), is("true"));
    }

    @Test
    public void profunctorProperties() {
        assertThat(CHECK_MULTIPLICATION.<String>diMapL(Integer::parseInt).apply("2").apply(3).apply(6), is(true));
        assertThat(CHECK_MULTIPLICATION.diMapR(f -> f.fmap(g -> g.andThen(Object::toString))).apply(2).apply(3).apply(6), is("true"));
        assertThat(CHECK_MULTIPLICATION.diMap((Fn1<String, Integer>) Integer::parseInt,
                                              f -> f.fmap(g -> g.andThen(Object::toString)))
                           .apply("2")
                           .apply(3)
                           .apply(6), is("true"));
    }
}
