package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.EmptyIterableSupport;
import testsupport.traits.FiniteIteration;
import testsupport.traits.ImmutableIteration;
import testsupport.traits.Laziness;

import java.util.Iterator;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Reverse.reverse;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class ReverseTest {

    @TestTraits({Laziness.class, ImmutableIteration.class, FiniteIteration.class, EmptyIterableSupport.class})
    public Reverse<?> createTestSubject() {
        return reverse();
    }

    @Test
    public void iteratesElementsOfAnIterableBackwards() {
        Iterable<String> words = asList("the", "rain", "in", "Spain");
        Iterable<String> reversed = reverse(words);

        assertThat(reversed, iterates("Spain", "in", "rain", "the"));
    }

    @Test
    @SuppressWarnings({"unchecked", "ResultOfMethodCallIgnored"})
    public void doesNotBeginReversingUntilIterated() {
        Iterable<Integer> mockIterable = mock(Iterable.class);
        Iterator<Integer> mockIterator = mock(Iterator.class);

        when(mockIterable.iterator()).thenReturn(mockIterator);

        Iterator<Integer> lazyIterable = reverse(mockIterable).iterator();
        lazyIterable.hasNext();

        verify(mockIterator).hasNext();
        verify(mockIterator, never()).next();
    }

    @Test
    public void doubleReverseIsNoOp() {
        assertThat(reverse(reverse(asList(1, 2, 3))), iterates(1, 2, 3));
    }
}
