package com.jnape.palatable.lambda.functions.specialized;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class BiPredicateTest {

    @Test
    public void jufBiPredicateTest() {
        BiPredicate<String, String> equals = String::equals;

        assertTrue(equals.apply("abc", "abc"));
        assertFalse(equals.apply("abc", ""));
        assertFalse(equals.apply("", "abc"));
    }

    @Test
    public void jufBiPredicateAnd() {
        BiPredicate<Integer, Integer> bothOdd     = (x, y) -> x % 2 == 1 && y % 2 == 1;
        BiPredicate<Integer, Integer> greaterThan = (x, y) -> x.compareTo(y) > 0;

        BiPredicate<Integer, Integer> conjunction = bothOdd.and(greaterThan);

        assertTrue(conjunction.apply(3, 1));
        assertFalse(conjunction.apply(3, 2));
        assertFalse(conjunction.apply(3, 5));
        assertFalse(conjunction.apply(4, 1));
    }

    @Test
    public void jufBiPredicateOr() {
        BiPredicate<Integer, Integer> bothOdd     = (x, y) -> x % 2 == 1 && y % 2 == 1;
        BiPredicate<Integer, Integer> greaterThan = (x, y) -> x.compareTo(y) > 0;

        BiPredicate<Integer, Integer> disjunction = bothOdd.or(greaterThan);

        assertTrue(disjunction.apply(3, 2));
        assertTrue(disjunction.apply(1, 3));
        assertFalse(disjunction.apply(1, 2));
    }

    @Test
    public void jufBiPredicateNegate() {
        BiPredicate<String, String> equals = String::equals;

        assertTrue(equals.apply("a", "a"));
        assertFalse(equals.apply("b", "a"));
        assertFalse(equals.negate().apply("a", "a"));
        assertTrue(equals.negate().apply("b", "a"));
    }

    @Test
    public void flip() {
        BiPredicate<String, String> equals = String::equals;
        assertThat(equals.flip(), instanceOf(BiPredicate.class));
    }

    @Test
    public void fromPredicate() {
        java.util.function.BiPredicate<String, String> jufBiPredicate = Object::equals;
        BiPredicate<String, String>                    biPredicate    = BiPredicate.fromBiPredicate(jufBiPredicate);
        assertTrue(biPredicate.apply("a", "a"));
    }

    @Test
    public void toPredicate() {
        BiPredicate<String, String>                    biPredicate    = Object::equals;
        java.util.function.BiPredicate<String, String> jufBiPredicate = biPredicate.toBiPredicate();
        assertTrue(jufBiPredicate.test("a", "a"));
    }
}