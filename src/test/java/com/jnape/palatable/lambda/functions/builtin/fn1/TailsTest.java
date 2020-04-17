package com.jnape.palatable.lambda.functions.builtin.fn1;

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

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Last.last;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Repeat.repeat;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Tails.tails;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Take.take;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class TailsTest {

    @TestTraits({EmptyIterableSupport.class, InfiniteIterableSupport.class, FiniteIteration.class, ImmutableIteration.class, Laziness.class})
    public Fn1<? extends Iterable<?>, ? extends Iterable<?>> testSubject() {
        return tails();
    }

    @Test
    public void empty() {
        assertThat(tails(emptyList()), iterates(emptyList()));
    }

    @Test
    public void nonEmpty() {
        assertThat(tails(singletonList(1)), iterates(singletonList(1), emptyList()));
        assertThat(tails(asList(1, 2, 3, 4, 5)), iterates(asList(1, 2, 3, 4, 5),
                                                          asList(2, 3, 4, 5),
                                                          asList(3, 4, 5),
                                                          asList(4, 5),
                                                          singletonList(5),
                                                          emptyList()));
    }

    @Test
    public void largeNumberOfElements() {
        assertEquals(just(emptyList()), last(tails(take(10_000, repeat(1)))));
    }
}