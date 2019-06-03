package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.FiniteIteration;
import testsupport.traits.ImmutableIteration;
import testsupport.traits.InfiniteIterableSupport;
import testsupport.traits.Laziness;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Intersperse.intersperse;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.isEmpty;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class IntersperseTest {

    @TestTraits({Laziness.class, FiniteIteration.class, ImmutableIteration.class, InfiniteIterableSupport.class})
    public Fn1<Iterable<Object>, Iterable<Object>> testSubject() {
        return intersperse(0);
    }

    @Test
    public void interspersesBetweenElementsInIterable() {
        assertThat(intersperse(0, asList(1, 2, 3)), iterates(1, 0, 2, 0, 3));
    }

    @Test
    public void doesNotIntersperseSingletonIterable() {
        assertThat(intersperse(0, asList(1)), iterates(1));
    }

    @Test
    public void doesNotIntersperseEmptyIterable() {
        assertThat(intersperse(0, emptyList()), isEmpty());
    }
}