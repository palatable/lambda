package com.jnape.palatable.lambda.functions.builtin.dyadic;

import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.functions.specialized.Predicate;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.EmptyIterableSupport;
import testsupport.traits.FiniteIteration;
import testsupport.traits.ImmutableIteration;
import testsupport.traits.Laziness;

import static com.jnape.palatable.lambda.functions.builtin.dyadic.DropWhile.dropWhile;
import static com.jnape.palatable.lambda.functions.builtin.monadic.Always.always;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.isEmpty;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class DropWhileTest {

    @TestTraits({Laziness.class, ImmutableIteration.class, FiniteIteration.class, EmptyIterableSupport.class})
    public MonadicFunction<Iterable<Object>, Iterable<Object>> createTestSubject() {
        return dropWhile(always(true));
    }

    @Test
    public void dropsElementsWhilePredicateIsTrue() {
        Predicate<Integer> lessThan3 = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer integer) {
                return integer < 3;
            }
        };
        assertThat(dropWhile(lessThan3, asList(1, 2, 3)), iterates(3));
    }

    @Test
    public void dropsAllElementsIfPredicateNeverFails() {
        assertThat(dropWhile(always(true), asList(1, 2, 3)), isEmpty());
    }

    @Test
    public void dropsNoElementsIfPredicateImmediatelyFails() {
        assertThat(dropWhile(always(false), asList(1, 2, 3)), iterates(1, 2, 3));
    }
}
