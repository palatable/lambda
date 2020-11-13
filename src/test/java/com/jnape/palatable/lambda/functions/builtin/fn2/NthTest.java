package com.jnape.palatable.lambda.functions.builtin.fn2;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn0.NaturalNumbers.naturalNumbers;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Nth.nth;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Take.take;
import static java.util.Collections.emptyList;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static testsupport.Constants.STACK_EXPLODING_NUMBER;

public class NthTest {
    @Test
    public void negativeNthIsNothing() {
        assertThat(nth(-5, naturalNumbers()), equalTo(nothing()));
    }

    @Test
    public void zerothIsNothing() {
        assertThat(nth(0, naturalNumbers()), equalTo(nothing()));
    }

    @Test
    public void nthFromEmpty() {
        assertThat(nth(5, emptyList()), equalTo(nothing()));
    }

    @Test
    public void nthAfterEnd() {
        assertThat(nth(5, take(3, naturalNumbers())), equalTo(nothing()));
    }

    @Test
    public void nthWithinRange() {
        assertThat(nth(5, take(10, naturalNumbers())), equalTo(just(5)));
    }

    @Test
    public void largeNthFromInfinite() {
        assertThat(nth(STACK_EXPLODING_NUMBER, naturalNumbers()), equalTo(just(STACK_EXPLODING_NUMBER)));
    }
}