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

import static com.jnape.palatable.lambda.functions.builtin.fn1.Init.init;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static testsupport.matchers.IterableMatcher.isEmpty;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class InitTest {

    @TestTraits({EmptyIterableSupport.class, InfiniteIterableSupport.class, Laziness.class, ImmutableIteration.class, FiniteIteration.class})
    public Fn1<? extends Iterable<?>, ? extends Iterable<?>> testSubject() {
        return init();
    }

    @Test
    public void empty() {
        assertThat(init(emptyList()), isEmpty());
    }

    @Test
    public void nonEmpty() {
        assertThat(init(singletonList(1)), isEmpty());
        assertThat(init(asList(1, 2, 3)), iterates(1, 2));
    }
}