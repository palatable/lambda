package com.jnape.palatable.lambda.monoid;

import org.junit.Test;

import java.util.List;
import java.util.Optional;

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

    @Test
    public void foldMap() {
        Monoid<Integer> sum = monoid((x, y) -> x + y, 0);
        List<Optional<Integer>> optionalInts = asList(Optional.of(1), Optional.of(2), Optional.empty(), Optional.of(3), Optional.empty());
        assertEquals((Integer) 6, sum.foldMap(optX -> optX.orElse(0), optionalInts));
    }
}