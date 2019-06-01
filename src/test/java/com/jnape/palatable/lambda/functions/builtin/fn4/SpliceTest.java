package com.jnape.palatable.lambda.functions.builtin.fn4;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.EmptyIterableSupport;
import testsupport.traits.FiniteIteration;
import testsupport.traits.ImmutableIteration;
import testsupport.traits.Laziness;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Repeat.repeat;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Take.take;
import static com.jnape.palatable.lambda.functions.builtin.fn4.Splice.splice;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class SpliceTest {

    @TestTraits({FiniteIteration.class, EmptyIterableSupport.class, ImmutableIteration.class, Laziness.class})
    public Fn1<Iterable<Object>, Iterable<Object>> createTestSubject() {
        return splice(5, 3, asList(100, 200, 300, 400, 500));
    }

    @Test
    public void spliceIsAttachedToEndIfOriginalIsNotLongEnough() {
        assertThat(splice(100, 0, asList(4, 5, 6), asList(1, 2, 3)),
                iterates(1, 2, 3, 4, 5, 6));
    }

    @Test
    public void splicingIntoEmptyIterableEqualsReplacement() {
        assertThat(splice(100, 100, asList(1, 2, 3), emptyList()),
                iterates(1, 2, 3));
    }

    @Test
    public void splicesEmptyWithoutReplacing() {
        assertThat(splice(3, 0, emptyList(), asList(1, 2, 3, 4, 5)),
                iterates(1, 2, 3, 4, 5));
    }

    @Test
    public void spliceEmptyAndReplaces() {
        assertThat(splice(2, 2, emptyList(), asList(1, 2, 3, 4, 5)),
                iterates(1, 2, 5));
    }

    @Test
    public void splicesOntoFrontWithoutReplacing() {
        assertThat(splice(0, 0, asList(100, 200, 300), asList(1, 2, 3, 4, 5, 6, 7, 8)),
                iterates(100, 200, 300, 1, 2, 3, 4, 5, 6, 7, 8));
    }

    @Test
    public void splicesOntoFrontAndReplaces() {
        assertThat(splice(0, 5, asList(100, 200, 300), asList(1, 2, 3, 4, 5, 6, 7, 8)),
                iterates(100, 200, 300, 6, 7, 8));
    }

    @Test
    public void splicesIntoMiddleWithoutReplacing() {
        assertThat(splice(3, 0, asList(100, 200, 300), asList(1, 2, 3, 4, 5, 6, 7, 8)),
                iterates(1, 2, 3, 100, 200, 300, 4, 5, 6, 7, 8));
    }

    @Test
    public void splicesIntoMiddleAndReplaces() {
        assertThat(splice(3, 3, asList(100, 200, 300), asList(1, 2, 3, 4, 5, 6, 7, 8)),
                iterates(1, 2, 3, 100, 200, 300, 7, 8));
    }

    @Test
    public void splicesOntoEndWithoutReplacing() {
        assertThat(splice(0, 0, asList(100, 200, 300), asList(1, 2, 3, 4, 5, 6, 7, 8)),
                iterates(100, 200, 300, 1, 2, 3, 4, 5, 6, 7, 8));
    }

    @Test
    public void spliceReplacesEntireOriginal() {
        assertThat(splice(0, 100, asList(100, 200, 300), asList(1, 2, 3, 4, 5, 6, 7, 8)),
                iterates(100, 200, 300));
    }

    @Test
    public void spliceInfiniteIntoFiniteOriginal() {
        assertThat(take(10, splice(5, 0, repeat(100), asList(1, 2, 3, 4, 5, 6, 7, 8))),
                iterates(1, 2, 3, 4, 5, 100, 100, 100, 100, 100));
    }

    @Test
    public void spliceFiniteIntoInfiniteOriginal() {
        assertThat(take(10, splice(3, 0, asList(1, 2, 3), repeat(100))),
                iterates(100, 100, 100, 1, 2, 3, 100, 100, 100, 100));
    }

    @Test
    public void spliceInfiniteIntoInfiniteOriginal() {
        assertThat(take(10, splice(3, 0, repeat(100), repeat(1))),
                iterates(1, 1, 1, 100, 100, 100, 100, 100, 100, 100));
    }
}
