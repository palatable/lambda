package com.jnape.palatable.lambda.monoid;

import org.junit.Test;

import static com.jnape.palatable.lambda.monoid.Monoid.monoid;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class MonoidTest {

    @Test
    public void reduceLeft() {
        Monoid<Integer> sum = monoid((x, y) -> x + y, 0);
        assertEquals((Integer) 6, sum.reduceLeft(asList(1, 2, 3)));
    }

    @Test
    public void reduceRight() {
        Monoid<Integer> sum = monoid((x, y) -> x + y, 0);
        assertEquals((Integer) 6, sum.reduceRight(asList(1, 2, 3)));
    }
}