package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.monad.transformer.builtin.IterateT;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.NaturalNumbersM.naturalNumbersM;
import static com.jnape.palatable.lambda.functions.builtin.fn2.NthM.nthM;
import static com.jnape.palatable.lambda.functions.builtin.fn2.TakeM.takeM;
import static com.jnape.palatable.lambda.functor.builtin.Identity.pureIdentity;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static testsupport.Constants.STACK_EXPLODING_NUMBER;

public class NthMTest {

    @Test
    public void negativeNthIsNothing() {
        Identity<Maybe<Integer>> actual = nthM(-5, naturalNumbersM(pureIdentity())).coerce();
        assertThat(actual.runIdentity(), equalTo(nothing()));
    }

    @Test
    public void zerothIsNothing() {
        Identity<Maybe<Integer>> actual = nthM(0, naturalNumbersM(pureIdentity())).coerce();
        assertThat(actual.runIdentity(), equalTo(nothing()));
    }

    @Test
    public void nthFromEmpty() {
        Identity<Maybe<Integer>> actual = nthM(5, IterateT.<Identity<?>, Integer>empty(pureIdentity())).coerce();
        assertThat(actual.runIdentity(), equalTo(nothing()));
    }

    @Test
    public void nthAfterEnd() {
        Identity<Maybe<Integer>> actual = nthM(5, takeM(3, naturalNumbersM(pureIdentity()))).coerce();
        assertThat(actual.runIdentity(), equalTo(nothing()));
    }

    @Test
    public void nthWithinRange() {
        Identity<Maybe<Integer>> actual = nthM(5, takeM(10, naturalNumbersM(pureIdentity()))).coerce();
        assertThat(actual.runIdentity(), equalTo(just(5)));
    }

    @Test
    public void largeNthFromInfinite() {
        Identity<Maybe<Integer>> actual = nthM(STACK_EXPLODING_NUMBER, naturalNumbersM(pureIdentity())).coerce();
        assertThat(actual.runIdentity(), equalTo(just(STACK_EXPLODING_NUMBER)));
    }

}