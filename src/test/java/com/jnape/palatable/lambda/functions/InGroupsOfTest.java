package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.MonadicFunction;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.EmptyIterableSupport;
import testsupport.traits.FiniteIteration;
import testsupport.traits.ImmutableIteration;
import testsupport.traits.Laziness;

import static com.jnape.palatable.lambda.functions.InGroupsOf.inGroupsOf;
import static com.jnape.palatable.lambda.staticfactory.IterableFactory.iterable;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

@SuppressWarnings("unchecked")
@RunWith(Traits.class)
public class InGroupsOfTest {

    @TestTraits({Laziness.class, EmptyIterableSupport.class, FiniteIteration.class, ImmutableIteration.class})
    public MonadicFunction<Iterable<Object>, Iterable<Iterable<Object>>> createTestSubject() {
        return inGroupsOf(2);
    }

    @Test
    public void evenlyDistributesGroupedElementsOverIterable() {
        Iterable<Integer> oneThroughSix = asList(1, 2, 3, 4, 5, 6);
        Iterable<Iterable<Integer>> groups = inGroupsOf(2, oneThroughSix);
        assertThat(groups, iterates(iterable(1, 2), iterable(3, 4), iterable(5, 6)));
    }

    @Test
    public void lastGroupIsUnfinishedIfIterableSizeNotEvenlyDivisibleByK() {
        Iterable<Integer> oneThroughFive = asList(1, 2, 3, 4, 5);
        Iterable<Iterable<Integer>> groups = inGroupsOf(2, oneThroughFive);
        assertThat(groups, iterates(iterable(1, 2), iterable(3, 4), iterable(5)));
    }
}
