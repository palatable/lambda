package com.jnape.palatable.lambda.functions.specialized;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PredicateTest {

    @Test
    public void happyPath() {
        Predicate<String> notEmpty = s -> !(s == null || s.length() == 0);

        assertTrue(notEmpty.apply("foo"));
        assertFalse(notEmpty.apply(""));
        assertFalse(notEmpty.apply(null));
    }

    @Test
    public void and() {
        Predicate<String> notEmpty  = s -> !(s == null || s.length() == 0);
        Predicate<String> lengthGt1 = s -> s.length() > 1;

        Predicate<String> conjunction = notEmpty.and(lengthGt1);

        assertTrue(conjunction.apply("fo"));
        assertFalse(conjunction.apply("f"));
        assertFalse(conjunction.apply(""));
        assertFalse(conjunction.apply(null));
    }

    @Test
    public void or() {
        Predicate<String> notEmpty  = s -> !(s == null || s.length() == 0);
        Predicate<String> lengthGt1 = s -> s != null && s.length() > 1;

        Predicate<String> disjunction = lengthGt1.or(notEmpty);

        assertTrue(disjunction.apply("fo"));
        assertTrue(disjunction.apply("f"));
        assertFalse(disjunction.apply(""));
        assertFalse(disjunction.apply(null));
    }

    @Test
    public void negate() {
        Predicate<Boolean> isTrue = x -> x;

        assertTrue(isTrue.apply(true));
        assertFalse(isTrue.apply(false));
        assertFalse(isTrue.negate().apply(true));
        assertTrue(isTrue.negate().apply(false));
    }

    @Test
    public void fromPredicate() {
        java.util.function.Predicate<String> jufPredicate = String::isEmpty;
        Predicate<String>                    predicate    = Predicate.fromPredicate(jufPredicate);
        assertFalse(predicate.apply("123"));
    }

    @Test
    public void toPredicate() {
        Predicate<String>                    predicate    = String::isEmpty;
        java.util.function.Predicate<String> jufPredicate = predicate.toPredicate();
        assertFalse(jufPredicate.test("123"));
    }
}