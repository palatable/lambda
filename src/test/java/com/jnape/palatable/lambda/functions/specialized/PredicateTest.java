package com.jnape.palatable.lambda.functions.specialized;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PredicateTest {

    @Test
    public void jufPredicateTest() {
        Predicate<String> notEmpty = s -> !(s == null || s.length() == 0);

        assertTrue(notEmpty.test("foo"));
        assertFalse(notEmpty.test(""));
        assertFalse(notEmpty.test(null));
    }

    @Test
    public void jufPredicateAnd() {
        Predicate<String> notEmpty = s -> !(s == null || s.length() == 0);
        Predicate<String> lengthGt1 = s -> s.length() > 1;

        Predicate<String> conjunction = notEmpty.and(lengthGt1);

        assertTrue(conjunction.test("fo"));
        assertFalse(conjunction.test("f"));
        assertFalse(conjunction.test(""));
        assertFalse(conjunction.test(null));
    }

    @Test
    public void jufPredicateOr() {
        Predicate<String> notEmpty = s -> !(s == null || s.length() == 0);
        Predicate<String> lengthGt1 = s -> s != null && s.length() > 1;

        Predicate<String> disjunction = lengthGt1.or(notEmpty);

        assertTrue(disjunction.test("fo"));
        assertTrue(disjunction.test("f"));
        assertFalse(disjunction.test(""));
        assertFalse(disjunction.test(null));
    }

    @Test
    public void jufPredicateNegate() {
        Predicate<Boolean> isTrue = x -> x;

        assertTrue(isTrue.test(true));
        assertFalse(isTrue.test(false));
        assertFalse(isTrue.negate().test(true));
        assertTrue(isTrue.negate().test(false));
    }
}