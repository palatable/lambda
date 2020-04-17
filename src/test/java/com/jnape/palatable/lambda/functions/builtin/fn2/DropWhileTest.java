package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.Predicate;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.EmptyIterableSupport;
import testsupport.traits.FiniteIteration;
import testsupport.traits.ImmutableIteration;
import testsupport.traits.Laziness;

import java.util.ArrayList;
import java.util.List;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn2.DropWhile.dropWhile;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static testsupport.matchers.IterableMatcher.isEmpty;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class DropWhileTest {

    @TestTraits({Laziness.class, ImmutableIteration.class, FiniteIteration.class, EmptyIterableSupport.class})
    public Fn1<Iterable<Object>, Iterable<Object>> createTestSubject() {
        return dropWhile(constantly(false));
    }

    @Test
    public void dropsElementsWhilePredicateIsTrue() {
        Predicate<Integer> lessThan3 = integer -> integer < 3;
        assertThat(dropWhile(lessThan3, asList(1, 2, 3)), iterates(3));
    }

    @Test
    public void dropsAllElementsIfPredicateNeverFails() {
        assertThat(dropWhile(constantly(true), asList(1, 2, 3)), isEmpty());
    }

    @Test
    public void dropsNoElementsIfPredicateImmediatelyFails() {
        assertThat(dropWhile(constantly(false), asList(1, 2, 3)), iterates(1, 2, 3));
    }

    @Test
    public void deforestingExecutesPredicatesInOrder() {
        List<Integer> innerInvocations = new ArrayList<>();
        List<Integer> outerInvocations = new ArrayList<>();
        dropWhile(y -> {
            outerInvocations.add(y);
            return true;
        }, dropWhile(x -> {
            innerInvocations.add(x);
            return x > 2;
        }, asList(1, 2, 3))).forEach(__ -> {});
        assertThat(innerInvocations, iterates(1, 2, 3));
        assertThat(outerInvocations, iterates(1, 2));
    }
}
