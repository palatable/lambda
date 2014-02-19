package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.Predicate;
import org.junit.Test;

import static com.jnape.palatable.lambda.builtin.monadic.Always.always;
import static com.jnape.palatable.lambda.functions.Take.take;
import static com.jnape.palatable.lambda.functions.Take.takeWhile;
import static com.jnape.palatable.lambda.staticfactory.IterableFactory.iterable;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

public class TakeTest {

    @Test
    public void takesElementsUpToLimit() {
        Iterable<String> names = iterable("Moe", "Larry", "Curly", "Shemp");
        assertThat(take(3, names), iterates("Moe", "Larry", "Curly"));
    }

    @Test
    public void iteratesEntireIterableIfLessElementsThanLimit() {
        Iterable<Integer> oneTwoThree = iterable(1, 2, 3);
        assertThat(take(4, oneTwoThree), iterates(1, 2, 3));
    }

    @Test
    public void takesNothingFromEmptyIterable() {
        assertThat(take(1, iterable()), iterates());
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
