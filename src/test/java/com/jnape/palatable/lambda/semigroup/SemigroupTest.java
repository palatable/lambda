package com.jnape.palatable.lambda.semigroup;

import org.junit.Test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static testsupport.functions.ExplainFold.explainFold;

public class SemigroupTest {

    @Test
    public void foldLeft() {
        Semigroup<String> foldFn = explainFold()::apply;
        assertEquals("(((0 + 1) + 2) + 3)", foldFn.foldLeft("0", asList("1", "2", "3")));
    }

    @Test
    public void foldRight() {
        Semigroup<String> foldFn = explainFold()::apply;
        assertEquals("(1 + (2 + (3 + 0)))", foldFn.foldRight("0", asList("1", "2", "3")).value());
    }
}