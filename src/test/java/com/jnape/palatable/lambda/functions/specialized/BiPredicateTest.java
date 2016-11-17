package com.jnape.palatable.lambda.functions.specialized;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BiPredicateTest {

    @Test
    public void jufBiPredicateTest() {
        BiPredicate<String, String> equals = String::equals;

        assertTrue(equals.test("abc", "abc"));
        assertFalse(equals.test("abc", ""));
        assertFalse(equals.test("", "abc"));
    }

    @Test
    public void jufBiPredicateAnd() {
        BiPredicate<Integer, Integer> bothOdd = (x, y) -> x % 2 == 1 && y % 2 == 1;
        BiPredicate<Integer, Integer> greaterThan = (x, y) -> x.compareTo(y) > 0;

        BiPredicate<Integer, Integer> conjunction = bothOdd.and(greaterThan);

        assertTrue(conjunction.test(3, 1));
        assertFalse(conjunction.test(3, 2));
        assertFalse(conjunction.test(3, 5));
        assertFalse(conjunction.test(4, 1));
    }

    @Test
    public void jufBiPredicateOr() {
        BiPredicate<Integer, Integer> bothOdd = (x, y) -> x % 2 == 1 && y % 2 == 1;
        BiPredicate<Integer, Integer> greaterThan = (x, y) -> x.compareTo(y) > 0;

        BiPredicate<Integer, Integer> disjunction = bothOdd.or(greaterThan);

        assertTrue(disjunction.test(3, 2));
        assertTrue(disjunction.test(1, 3));
        assertFalse(disjunction.test(1, 2));
    }

    @Test
    public void jufBiPredicateNegate() {
        BiPredicate<String, String> equals = String::equals;

        assertTrue(equals.test("a", "a"));
        assertFalse(equals.test("b", "a"));
        assertFalse(equals.negate().test("a", "a"));
        assertTrue(equals.negate().test("b", "a"));
    }
}