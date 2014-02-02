package com.jnape.palatable.lambda.functions;

import org.junit.Test;

import static com.jnape.palatable.lambda.functions.InGroupsOf.inGroupsOf;
import static com.jnape.palatable.lambda.staticfactory.IterableFactory.iterable;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

@SuppressWarnings("unchecked")
public class InGroupsOfTest {

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
