package com.jnape.palatable.lambda.semigroup;

import org.junit.Test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class SemigroupTest {

    @Test
    public void foldLeft() {
        Semigroup<Integer> sum = (x, y) -> x + y;
        assertEquals((Integer) 6, sum.foldLeft(0, asList(1, 2, 3)));
    }

    @Test
    public void foldRight() {
        Semigroup<Integer> sum = (x, y) -> x + y;
        assertEquals((Integer) 6, sum.foldRight(0, asList(1, 2, 3)));
    }
}