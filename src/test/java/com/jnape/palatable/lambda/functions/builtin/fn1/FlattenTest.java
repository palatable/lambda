package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn2.Map;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.EmptyIterableSupport;
import testsupport.traits.FiniteIteration;
import testsupport.traits.InfiniteIterableSupport;
import testsupport.traits.Laziness;

import java.util.Collection;
import java.util.Collections;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Flatten.flatten;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.isEmpty;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class FlattenTest {

    @TestTraits({Laziness.class, InfiniteIterableSupport.class, EmptyIterableSupport.class, FiniteIteration.class})
    public Fn1<Iterable<Integer>, Iterable<Integer>> testSubject() {
        return Flatten.<Integer>flatten().compose(Map.<Integer, Collection<Integer>>map(Collections::singletonList));
    }

    @Test
    public void flattensIterableOfEmptyIterables() {
        assertThat(flatten(asList(emptyList(), emptyList())), isEmpty());
    }

    @Test
    public void flattensSparseIterableOfPopulatedIterables() {
        assertThat(flatten(asList(emptyList(), asList(1, 2, 3), emptyList(), emptyList(), singleton(4), asList(5, 6), emptyList())),
                   iterates(1, 2, 3, 4, 5, 6));
    }
}