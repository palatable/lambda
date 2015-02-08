package com.jnape.palatable.lambda.continuation;

import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static com.jnape.palatable.lambda.continuation.Continuations.continuing;
import static org.hamcrest.MatcherAssert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

public class ContinuationIteratorTest {

    @Test
    public void iteratesContinuation() {
        Continuation<Integer> numbers = continuing(1, 2, 3);
        ContinuationIterator<Integer> continuationIterator = new ContinuationIterator<>(numbers);

        assertThat(() -> continuationIterator, iterates(1, 2, 3));
    }

    @Test(expected = NoSuchElementException.class)
    public void usesStandardSemanticFailuresIfIteratingPastLastContinuation() {
        new ContinuationIterator<Integer>(Optional::empty).next();
    }
}