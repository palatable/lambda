package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.EmptyIterableSupport;
import testsupport.traits.FiniteIteration;
import testsupport.traits.ImmutableIteration;
import testsupport.traits.InfiniteIterableSupport;
import testsupport.traits.Laziness;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Distinct.distinct;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class DistinctTest {

    @TestTraits({Laziness.class, InfiniteIterableSupport.class, EmptyIterableSupport.class, FiniteIteration.class, ImmutableIteration.class})
    public Distinct<?> testSubject() {
        return distinct();
    }

    @Test
    public void producesIterableOfOnlySingleElementOccurrences() {
        assertThat(distinct(asList(1, 2, 2, 3, 3, 3)), iterates(1, 2, 3));
    }
}