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

import static com.jnape.palatable.lambda.functions.builtin.fn1.Inits.inits;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class InitsTest {

    @TestTraits({Laziness.class, EmptyIterableSupport.class, InfiniteIterableSupport.class, FiniteIteration.class, ImmutableIteration.class})
    public Fn1<? extends Iterable<?>, ? extends Iterable<?>> testSubject() {
        return inits();
    }

    @Test
    public void empty() {
        assertThat(inits(emptyList()), iterates(emptyList()));
    }

    @Test
    public void nonEmpty() {
        assertThat(inits(singletonList(1)), iterates(emptyList(), singletonList(1)));
        assertThat(inits(asList(1, 2, 3, 4, 5)), iterates(emptyList(),
                                                          singletonList(1),
                                                          asList(1, 2),
                                                          asList(1, 2, 3),
                                                          asList(1, 2, 3, 4),
                                                          asList(1, 2, 3, 4, 5)));
    }
}