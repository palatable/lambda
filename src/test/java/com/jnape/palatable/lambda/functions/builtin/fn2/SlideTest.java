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

import static com.jnape.palatable.lambda.functions.builtin.fn2.Drop.drop;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Iterate.iterate;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Slide.slide;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Take.take;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.isEmpty;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class SlideTest {

    @TestTraits({ImmutableIteration.class, EmptyIterableSupport.class, FiniteIteration.class, InfiniteIterableSupport.class, Laziness.class})
    public Fn1<Iterable<Object>, Iterable<Iterable<Object>>> testSubject() {
        return slide(2);
    }

    @Test
    public void slidesAcrossIterable() {
        assertThat(slide(1, asList(1, 2, 3)), iterates(singletonList(1), singletonList(2), singletonList(3)));
        assertThat(slide(2, asList(1, 2, 3)), iterates(asList(1, 2), asList(2, 3)));
        assertThat(slide(3, asList(1, 2, 3)), iterates(asList(1, 2, 3)));
        assertThat(slide(4, asList(1, 2, 3)), isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void kMustBeGreaterThan0() {
        slide(0, emptyList());
    }

    @Test
    public void stackSafety() {
        Integer stackBlowingNumber = 50000;
        Iterable<Iterable<Integer>> xss = slide(2, take(stackBlowingNumber, iterate(x -> x + 1, 1)));
        assertThat(drop(stackBlowingNumber - 2, xss), iterates(asList(49999, 50000)));
    }
}