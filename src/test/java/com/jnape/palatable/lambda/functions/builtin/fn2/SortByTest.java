package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.EmptyIterableSupport;
import testsupport.traits.FiniteIteration;

import java.util.List;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn2.SortBy.sortBy;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class SortByTest {

    @TestTraits({FiniteIteration.class, EmptyIterableSupport.class})
    public Fn1<Iterable<Integer>, List<Integer>> testSubject() {
        return sortBy(id());
    }

    @Test
    public void sortsIterable() {
        assertThat(sortBy(id(), asList(2, 1, 3)), iterates(1, 2, 3));
    }
}