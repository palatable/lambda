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

import static com.jnape.palatable.lambda.functions.builtin.fn2.PrependAll.prependAll;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static testsupport.matchers.IterableMatcher.isEmpty;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class PrependAllTest {

    @TestTraits({Laziness.class, FiniteIteration.class, EmptyIterableSupport.class, ImmutableIteration.class, InfiniteIterableSupport.class})
    public Fn1<Iterable<Object>, Iterable<Object>> testSubject() {
        return prependAll(0);
    }

    @Test
    public void prependsValueToAllElementsInIterable() {
        Iterable<Integer> ints = asList(1, 2, 3);
        assertThat(prependAll(0, ints), iterates(0, 1, 0, 2, 0, 3));
    }

    @Test
    public void prependingToEmptyIterableIsStillEmpty() {
        assertThat(prependAll(0, emptyList()), isEmpty());
    }
}