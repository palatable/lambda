package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.EmptyIterableSupport;
import testsupport.traits.FiniteIteration;
import testsupport.traits.ImmutableIteration;
import testsupport.traits.Laziness;

import static com.jnape.palatable.lambda.functions.builtin.fn2.InGroupsOf.inGroupsOf;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class InGroupsOfTest {

    @TestTraits({Laziness.class, EmptyIterableSupport.class, FiniteIteration.class, ImmutableIteration.class})
    public Fn1<Iterable<Object>, Iterable<Iterable<Object>>> createTestSubject() {
        return inGroupsOf(2);
    }

    @Test
    public void evenlyDistributesGroupedElementsOverIterable() {
        Iterable<Integer>           oneThroughSix = asList(1, 2, 3, 4, 5, 6);
        Iterable<Iterable<Integer>> groups        = inGroupsOf(2, oneThroughSix);
        assertThat(groups, iterates(asList(1, 2), asList(3, 4), asList(5, 6)));
    }

    @Test
    public void lastGroupIsUnfinishedIfIterableSizeNotEvenlyDivisibleByK() {
        Iterable<Integer>           oneThroughFive = asList(1, 2, 3, 4, 5);
        Iterable<Iterable<Integer>> groups         = inGroupsOf(2, oneThroughFive);
        assertThat(groups, iterates(asList(1, 2), asList(3, 4), singletonList(5)));
    }
}
