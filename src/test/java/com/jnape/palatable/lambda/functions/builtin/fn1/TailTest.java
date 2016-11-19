package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.EmptyIterableSupport;
import testsupport.traits.FiniteIteration;
import testsupport.traits.ImmutableIteration;
import testsupport.traits.Laziness;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Tail.tail;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class TailTest {

    @TestTraits({Laziness.class, EmptyIterableSupport.class, FiniteIteration.class, ImmutableIteration.class})
    public Fn1<? extends Iterable, ?> createTraitsTestSubject() {
        return tail();
    }

    @Test
    public void skipsFirstElementOfNonEmptyList() {
        assertThat(tail(asList(1, 2, 3)), iterates(2, 3));
    }

    @Test
    public void isEmptyIfEmptyIterable() {
        assertThat(tail(emptyList()), iterates());
    }
}