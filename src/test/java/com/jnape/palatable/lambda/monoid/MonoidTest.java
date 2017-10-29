package com.jnape.palatable.lambda.monoid;

import com.jnape.palatable.lambda.adt.Maybe;
import org.junit.Test;

import java.util.List;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
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
        List<Maybe<Integer>> maybeInts = asList(just(1), just(2), nothing(), just(3), nothing());
        assertEquals((Integer) 6, sum.foldMap(maybeX -> maybeX.orElse(0), maybeInts));
    }
}