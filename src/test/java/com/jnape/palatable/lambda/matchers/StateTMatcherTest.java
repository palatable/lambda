package com.jnape.palatable.lambda.matchers;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.monad.transformer.builtin.StateT;
import org.junit.Test;
import testsupport.matchers.StateTMatcher;

import java.util.concurrent.atomic.AtomicInteger;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.io.IO.io;
import static com.jnape.palatable.lambda.monad.transformer.builtin.StateT.stateT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static testsupport.matchers.IOMatcher.yieldsValue;
import static testsupport.matchers.LeftMatcher.isLeftThat;
import static testsupport.matchers.RightMatcher.isRightThat;
import static testsupport.matchers.StateTMatcher.*;

public class StateTMatcherTest {

    @Test
    public void whenEvalWithMatcher() {
        assertThat(stateT(Either.right(1)),
                StateTMatcher.whenEvaluatedWith("0", isRightThat(equalTo(1))));
    }

    @Test
    public void whenExecWithMatcher() {
        assertThat(stateT(Either.right(1)),
                whenExecutedWith(left("0"), isRightThat(isLeftThat(equalTo("0")))));
    }

    @Test
    public void whenRunWithUsingTwoMatchers() {
        assertThat(stateT(Either.right(1)),
                whenRunWith(left("0"), isRightThat(equalTo(1)), isRightThat(isLeftThat(equalTo("0")))));
    }

    @Test
    public void whenRunWithUsingOneTupleMatcher() {
        assertThat(stateT(Either.right(1)),
                whenRunWith(left("0"), isRightThat(equalTo(tuple(1, left("0"))))));
    }

    @Test
    public void onlyRunsStateOnceWithTupleMatcher() {
        AtomicInteger count = new AtomicInteger(0);

        assertThat(StateT.gets(s -> io(count::incrementAndGet)),
                whenRunWith(0, yieldsValue(equalTo(tuple(1, 0)))));
        assertEquals(1, count.get());
    }
}