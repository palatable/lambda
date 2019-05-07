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

import static com.jnape.palatable.lambda.functions.builtin.fn1.Last.last;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Repeat.repeat;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Eq.eq;
import static com.jnape.palatable.lambda.functions.builtin.fn2.GTE.gte;
import static com.jnape.palatable.lambda.functions.builtin.fn2.MagnetizeBy.magnetizeBy;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Take.take;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.isEmpty;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class MagnetizeByTest {

    @TestTraits({EmptyIterableSupport.class, InfiniteIterableSupport.class, FiniteIteration.class, ImmutableIteration.class, Laziness.class})
    public Fn1<Iterable<Object>, Iterable<Iterable<Object>>> testSubject() {
        return magnetizeBy(eq());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void magnetizesElementsByPredicateOutcome() {
        assertThat(magnetizeBy(GTE.<Integer>gte(), emptyList()), isEmpty());
        assertThat(magnetizeBy(gte(), singletonList(1)), contains(iterates(1)));
        assertThat(magnetizeBy(gte(), asList(1, 2, 3, 2, 2, 3, 2, 1)),
                   contains(iterates(1, 2, 3),
                            iterates(2, 2, 3),
                            iterates(2),
                            iterates(1)));
    }

    @Test
    public void stackSafety() {
        int stackBlowingNumber = 10_000;
        assertThat(last(magnetizeBy((x, y) -> false, take(stackBlowingNumber, repeat(1)))).orElseThrow(AssertionError::new),
                   iterates(1));
    }
}