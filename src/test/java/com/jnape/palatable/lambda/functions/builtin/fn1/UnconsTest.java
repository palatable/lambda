package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.EmptyIterableSupport;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Uncons.uncons;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class UnconsTest {

    @TestTraits({EmptyIterableSupport.class})
    public Uncons<?> testSubject() {
        return uncons();
    }

    @Test
    public void nonEmptyIterable() {
        Iterable<Integer> numbers = asList(1, 2, 3);
        Tuple2<Integer, Iterable<Integer>> headAndTail = uncons(numbers).orElseThrow(AssertionError::new);

        assertEquals((Integer) 1, headAndTail._1());
        assertThat(headAndTail._2(), iterates(2, 3));
    }

    @Test
    public void emptyIterable() {
        assertEquals(nothing(), uncons(emptyList()));
    }
}