package com.jnape.palatable.lambda.continuation;

import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static com.jnape.palatable.lambda.adt.tuples.Tuple2.tuple;
import static com.jnape.palatable.lambda.continuation.Continuations.continuing;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

public class ContinuationTest {

    private static final Continuation<Integer> ONE_TWO_THREE = continuing(1, 2, 3);

    @Test
    public void isIterable() {
        assertThat(ONE_TWO_THREE, iterates(1, 2, 3));
    }

    @Test
    public void fmapRecursivelyComposesContinuationLineage() {
        assertThat(ONE_TWO_THREE.fmap(Object::toString), iterates("1", "2", "3"));
    }

    @Test
    public void thenContinuesOnToNextContinuationOnceEmpty() {
        Continuation<Integer> fourFiveSix = continuing(4, 5, 6);
        assertThat(ONE_TWO_THREE.then(fourFiveSix), iterates(1, 2, 3, 4, 5, 6));
    }

    @Test
    public void memoizesEveryResult() {
        AtomicInteger index = new AtomicInteger(0);

        Continuation<Integer> evilSideEffectDrivenContinuation = () ->
                Optional.of(tuple(index.incrementAndGet(), () ->
                        Optional.of(tuple(index.incrementAndGet(), Optional::empty))));

        Continuation<Integer> memoizingContinuation = evilSideEffectDrivenContinuation.memoized();

        assertThat(memoizingContinuation, iterates(1, 2));
        assertThat(memoizingContinuation, iterates(1, 2));
        assertThat(memoizingContinuation, iterates(1, 2));
    }

    @Test
    public void memoizationIsLazy() {
        AtomicInteger invocations = new AtomicInteger(0);
        Continuation<Integer> invocationCountingContinuation = continuing(1, 2, 3, 4, 5).fmap(i -> {
            invocations.incrementAndGet();
            return i;
        });

        invocationCountingContinuation.memoized();

        assertThat(invocations.get(), is(0));
    }
}