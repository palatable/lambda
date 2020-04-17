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

import java.util.Collections;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Snoc.snoc;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class SnocTest {

    @TestTraits({Laziness.class, EmptyIterableSupport.class, ImmutableIteration.class, FiniteIteration.class, InfiniteIterableSupport.class})
    public Fn1<Iterable<Object>, Iterable<Object>> testSubject() {
        return snoc(1);
    }

    @Test
    public void appendToEmptyIterable() {
        assertThat(snoc(1, Collections::emptyIterator), iterates(1));
    }

    @Test
    public void appendToNonEmptyIterable() {
        assertThat(snoc(4, asList(1, 2, 3)), iterates(1, 2, 3, 4));
    }

    @Test
    public void deforestingOrder() {
        assertThat(snoc(3, snoc(2, snoc(1, emptyList()))), iterates(1, 2, 3));
    }
}