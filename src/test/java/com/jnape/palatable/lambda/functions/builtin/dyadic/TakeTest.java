package com.jnape.palatable.lambda.functions.builtin.dyadic;

import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.EmptyIterableSupport;
import testsupport.traits.FiniteIteration;
import testsupport.traits.ImmutableIteration;
import testsupport.traits.Laziness;

import static com.jnape.palatable.lambda.functions.builtin.dyadic.Take.take;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class TakeTest {

    @TestTraits({FiniteIteration.class, EmptyIterableSupport.class, ImmutableIteration.class, Laziness.class})
    public MonadicFunction<Iterable<Object>, Iterable<Object>> createTestSubject() {
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
        assertThat(take(1, asList()), iterates());
    }
}
