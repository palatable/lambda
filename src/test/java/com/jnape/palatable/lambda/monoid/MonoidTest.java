package com.jnape.palatable.lambda.monoid;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import org.junit.Test;

import java.util.List;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.monoid.Monoid.monoid;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static testsupport.functions.ExplainFold.explainFold;

public class MonoidTest {

    @Test
    public void reduceLeft() {
        Monoid<Integer> sum = monoid(Integer::sum, 0);
        assertEquals((Integer) 6, sum.reduceLeft(asList(1, 2, 3)));
    }

    @Test
    public void reduceRight() {
        Monoid<Integer> sum = monoid(Integer::sum, 0);
        assertEquals((Integer) 6, sum.reduceRight(asList(1, 2, 3)));
    }

    @Test
    public void foldRight() {
        Lazy<String> lazyString = monoid(explainFold()::apply, "0")
                .foldRight("4", asList("1", "2", "3"));
        assertEquals("(1 + (2 + (3 + (4 + 0))))", lazyString.value());
    }

    @Test
    public void foldMap() {
        Monoid<Integer>      sum       = monoid(Integer::sum, 0);
        List<Maybe<Integer>> maybeInts = asList(just(1), just(2), nothing(), just(3), nothing());
        assertEquals((Integer) 6, sum.foldMap(maybeX -> maybeX.orElse(0), maybeInts));
    }
}