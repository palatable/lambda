package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.io.IO;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn2.FindM.findM;
import static com.jnape.palatable.lambda.functions.builtin.fn2.GTE.gte;
import static com.jnape.palatable.lambda.functions.builtin.fn2.TakeM.takeM;
import static com.jnape.palatable.lambda.functor.builtin.Identity.pureIdentity;
import static com.jnape.palatable.lambda.io.IO.pureIO;
import static com.jnape.palatable.lambda.monad.transformer.builtin.IterateT.empty;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static testsupport.Constants.STACK_EXPLODING_NUMBER;
import static com.jnape.palatable.lambda.functions.builtin.fn1.NaturalNumbersM.naturalNumbersM;
import static testsupport.matchers.IOMatcher.yieldsValue;

public class FindMTest {
    @Test
    public void findsSomething() {
        Identity<Maybe<Integer>> actual = findM(gte(4), takeM(10, naturalNumbersM(pureIdentity())));
        assertThat(actual.runIdentity(), equalTo(just(4)));
    }

    @Test
    public void findsNothingWhenEmpty() {
        Identity<Maybe<Integer>> actual = findM(constantly(true), empty(pureIdentity()));
        assertThat(actual.runIdentity(), equalTo(nothing()));
    }

    @Test
    public void findsNothingWhenNothingMatches() {
        Identity<Maybe<Integer>> actual = findM(gte(14), takeM(10, naturalNumbersM(pureIdentity())));
        assertThat(actual.runIdentity(), equalTo(nothing()));
    }

    @Test
    public void findsANeedleInAHaystack() {
        IO<Maybe<Integer>> actual = findM(gte(STACK_EXPLODING_NUMBER), naturalNumbersM(pureIO()));
        assertThat(actual, yieldsValue(equalTo(just(STACK_EXPLODING_NUMBER))));
    }
}