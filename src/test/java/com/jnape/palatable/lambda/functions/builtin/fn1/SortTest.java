package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.traitor.annotations.TestTraits;
import org.junit.Test;
import testsupport.traits.EmptyIterableSupport;
import testsupport.traits.FiniteIteration;

import java.util.List;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Sort.sort;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

public class SortTest {

    @TestTraits({FiniteIteration.class, EmptyIterableSupport.class})
    public Fn1<Iterable<Integer>, List<Integer>> testSubject() {
        return sort();
    }

    @Test
    public void sortsIterable() {
        assertThat(sort(asList(2, 1, 3)), iterates(1, 2, 3));
    }
}