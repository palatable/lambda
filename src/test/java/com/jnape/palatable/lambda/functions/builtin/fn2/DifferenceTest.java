package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.EmptyIterableSupport;
import testsupport.traits.FiniteIteration;
import testsupport.traits.ImmutableIteration;
import testsupport.traits.InfiniteIterableSupport;
import testsupport.traits.Laziness;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Difference.difference;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.isEmpty;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class DifferenceTest {

    @TestTraits({Laziness.class, InfiniteIterableSupport.class, EmptyIterableSupport.class, FiniteIteration.class, ImmutableIteration.class})
    public Fn1<Iterable<Integer>, Iterable<Integer>> testSubject() {
        return Difference.<Integer>difference().flip().apply(asList(1, 2, 3));
    }

    @Test
    public void semigroup() {
        assertThat(difference(emptyList(), emptyList()), isEmpty());
        assertThat(difference(asList(1, 2, 3), emptyList()), iterates(1, 2, 3));
        assertThat(difference(asList(1, 2, 2, 3), emptyList()), iterates(1, 2, 3));
        assertThat(difference(emptyList(), asList(1, 2, 3)), isEmpty());
        assertThat(difference(asList(1, 2, 3), singletonList(4)), iterates(1, 2, 3));
        assertThat(difference(asList(1, 2, 3), asList(2, 4)), iterates(1, 3));
    }
}