package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.MonadicFunction;
import com.jnape.palatable.lambda.Predicate;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.EmptyIterableSupport;
import testsupport.traits.FiniteIteration;
import testsupport.traits.ImmutableIteration;
import testsupport.traits.Laziness;

import static com.jnape.palatable.lambda.builtin.monadic.Always.always;
import static com.jnape.palatable.lambda.functions.TakeWhile.takeWhile;
import static com.jnape.palatable.lambda.staticfactory.IterableFactory.iterable;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class TakeWhileTest {

    @TestTraits({FiniteIteration.class, EmptyIterableSupport.class, ImmutableIteration.class, Laziness.class})
    public MonadicFunction<Iterable<Object>, Iterable<Object>> createTestObject() {
        return takeWhile(always(true));
    }

    @Test
    public void takesElementsWhilePredicateIsTrue() {
        Predicate<Integer> lessThan3 = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer integer) {
                return integer < 3;
            }
        };
        Iterable<Integer> numbers = takeWhile(lessThan3, iterable(1, 2, 3, 4, 5));
        assertThat(numbers, iterates(1, 2));
    }

    @Test
    public void takesAllElementsIfPredicateNeverFails() {
        String[] requirements = {"fast", "good", "cheap"};
        assertThat(
                takeWhile(always(true), iterable(requirements)),
                iterates(requirements)
        );
    }
}
