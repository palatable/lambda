package com.jnape.palatable.lambda.semigroup.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.EmptyIterableSupport;
import testsupport.traits.FiniteIteration;
import testsupport.traits.InfiniteIterableSupport;
import testsupport.traits.Laziness;

import static com.jnape.palatable.lambda.semigroup.builtin.Intersection.intersection;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static testsupport.matchers.IterableMatcher.isEmpty;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class IntersectionTest {

    @TestTraits({Laziness.class, InfiniteIterableSupport.class, EmptyIterableSupport.class, FiniteIteration.class})
    public Fn1<Iterable<Integer>, Iterable<Integer>> testSubject() {
        return intersection(asList(0, 1, 2, 3));
    }

    @Test
    public void intersectionOfEmptyOnEitherSideIsEmpty() {
        assertThat(intersection(emptyList(), singletonList(1)), isEmpty());
        assertThat(intersection(singletonList(1), emptyList()), isEmpty());
        assertThat(intersection(emptyList(), emptyList()), isEmpty());
    }

    @Test
    public void intersectionIsCommonElementsAcrossIterables() {
        assertThat(intersection(asList(1, 2, 3), asList(1, 2, 3)), iterates(1, 2, 3));
        assertThat(intersection(asList(1, 2, 3), asList(2, 3, 4)), iterates(2, 3));
        assertThat(intersection(singletonList(1), singletonList(2)), isEmpty());
        assertThat(intersection(asList(1, 2, 3, 3), singletonList(3)), iterates(3));
    }
}