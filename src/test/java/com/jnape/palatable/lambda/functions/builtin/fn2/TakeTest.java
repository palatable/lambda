package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.EmptyIterableSupport;
import testsupport.traits.FiniteIteration;
import testsupport.traits.ImmutableIteration;
import testsupport.traits.Laziness;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Take.take;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class TakeTest {

    @TestTraits({FiniteIteration.class, EmptyIterableSupport.class, ImmutableIteration.class, Laziness.class})
    public Fn1<Iterable<Object>, Iterable<Object>> createTestSubject() {
        return take(10);
    }

    @Test
    public void takesElementsUpToLimit() {
        Iterable<String> names = asList("Moe", "Larry", "Curly", "Shemp");
        assertThat(take(3, names), iterates("Moe", "Larry", "Curly"));
    }

    @Test
    public void iteratesEntireIterableIfLessElementsThanLimit() {
        Iterable<Integer> oneTwoThree = asList(1, 2, 3);
        assertThat(take(4, oneTwoThree), iterates(1, 2, 3));
    }

    @Test
    public void takesNothingFromEmptyIterable() {
        assertThat(take(1, emptyList()), iterates());
    }
}
