package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.Predicate;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.EmptyIterableSupport;
import testsupport.traits.FiniteIteration;
import testsupport.traits.ImmutableIteration;
import testsupport.traits.Laziness;

import java.util.ArrayList;
import java.util.List;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Force.force;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Filter.filter;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class FilterTest {

    @TestTraits({Laziness.class, EmptyIterableSupport.class, FiniteIteration.class, ImmutableIteration.class})
    public Fn1<? extends Iterable<?>, ?> testSubject() {
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

    @Test
    public void deforestingExecutesPredicatesInOrder() {
        List<Integer> innerInvocations = new ArrayList<>();
        List<Integer> outerInvocations = new ArrayList<>();
        force(filter(y -> {
            outerInvocations.add(y);
            return true;
        }, filter(x -> {
            innerInvocations.add(x);
            return x % 2 == 0;
        }, asList(1, 2, 3))));
        assertThat(innerInvocations, iterates(1, 2, 3));
        assertThat(outerInvocations, iterates(2));
    }
}
