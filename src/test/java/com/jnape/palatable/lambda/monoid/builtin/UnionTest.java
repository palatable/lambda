package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.framework.Subjects;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.Deforesting;
import testsupport.traits.EmptyIterableSupport;
import testsupport.traits.FiniteIteration;
import testsupport.traits.ImmutableIteration;
import testsupport.traits.InfiniteIterableSupport;
import testsupport.traits.Laziness;

import static com.jnape.palatable.lambda.monoid.builtin.Union.union;
import static com.jnape.palatable.traitor.framework.Subjects.subjects;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static testsupport.matchers.IterableMatcher.isEmpty;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class UnionTest {

    @TestTraits({Laziness.class, InfiniteIterableSupport.class, EmptyIterableSupport.class, FiniteIteration.class, ImmutableIteration.class, Deforesting.class})
    public Subjects<Fn1<Iterable<Integer>, Iterable<Integer>>> testSubject() {
        return subjects(union(asList(1, 2, 3)), Union.<Integer>union().flip().apply(asList(1, 2, 3)));
    }

    @Test
    public void monoid() {
        assertThat(union().identity(), isEmpty());

        assertThat(union(emptyList(), emptyList()), isEmpty());
        assertThat(union(asList(1, 2), emptyList()), iterates(1, 2));
        assertThat(union(emptyList(), singletonList(3)), iterates(3));
        assertThat(union(asList(1, 2), singletonList(3)), iterates(1, 2, 3));
        assertThat(union(asList(1, 2, 2), singletonList(3)), iterates(1, 2, 3));
        assertThat(union(asList(1, 2), asList(1, 2, 3)), iterates(1, 2, 3));
    }
}