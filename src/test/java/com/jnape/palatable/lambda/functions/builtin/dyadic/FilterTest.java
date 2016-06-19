package com.jnape.palatable.lambda.functions.builtin.dyadic;

import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.functions.specialized.Predicate;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.EmptyIterableSupport;
import testsupport.traits.FiniteIteration;
import testsupport.traits.ImmutableIteration;
import testsupport.traits.Laziness;

import static com.jnape.palatable.lambda.functions.builtin.dyadic.Filter.filter;
import static com.jnape.palatable.lambda.functions.builtin.monadic.Constantly.constantly;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class FilterTest {

    @TestTraits({Laziness.class, EmptyIterableSupport.class, FiniteIteration.class, ImmutableIteration.class})
    public MonadicFunction<? extends Iterable, ?> createTraitsTestSubject() {
        return filter(constantly(true));
    }

    @Test
    public void filtersOutMatchingElements() {
        Predicate<Integer> evens = integer -> integer % 2 == 0;
        assertThat(
                filter(evens, asList(1, 2, 3, 4, 5, 6)),
                iterates(2, 4, 6)
        );
    }
}
