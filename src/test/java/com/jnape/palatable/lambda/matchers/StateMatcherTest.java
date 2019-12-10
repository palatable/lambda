package com.jnape.palatable.lambda.matchers;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.functor.builtin.State.state;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static testsupport.matchers.LeftMatcher.isLeftThat;
import static testsupport.matchers.RightMatcher.isRightThat;
import static testsupport.matchers.StateMatcher.whenEvaluatedWith;
import static testsupport.matchers.StateMatcher.whenExecutedWith;
import static testsupport.matchers.StateMatcher.whenRunWith;

public class StateMatcherTest {

    @Test
    public void whenEvalWithMatcher() {
        assertThat(state(right(1)),
                   whenEvaluatedWith("0", isRightThat(equalTo(1))));
    }

    @Test
    public void whenExecWithMatcher() {
        assertThat(state(right(1)),
                   whenExecutedWith(left("0"), isLeftThat(equalTo("0"))));
    }

    @Test
    public void whenRunWithMatcher() {
        assertThat(state(right(1)),
                   whenRunWith(left("0"), isRightThat(equalTo(1)), isLeftThat(equalTo("0"))));
    }
}