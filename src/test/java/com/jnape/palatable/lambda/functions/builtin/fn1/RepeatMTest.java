package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.functions.builtin.fn1.RepeatM.repeatM;
import static com.jnape.palatable.lambda.functions.builtin.fn2.NthM.nthM;
import static com.jnape.palatable.lambda.functions.builtin.fn2.TakeM.takeM;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static testsupport.Constants.STACK_EXPLODING_NUMBER;
import static testsupport.matchers.IterateTMatcher.iterates;

public class RepeatMTest {

    @Test
    public void repeatsAThing() {
        assertThat(takeM(10, repeatM(new Identity<>(1))), iterates(1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    }

    @Test
    public void repeatsALotOfThings() {
        Identity<Maybe<Integer>> actual = nthM(STACK_EXPLODING_NUMBER, repeatM(new Identity<>(1))).coerce();
        assertThat(actual.runIdentity(), equalTo(just(1)));
    }
}