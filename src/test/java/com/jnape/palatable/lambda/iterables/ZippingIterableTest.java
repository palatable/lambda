package com.jnape.palatable.lambda.iterables;

import com.jnape.palatable.lambda.DyadicFunction;
import com.jnape.palatable.lambda.tuples.Tuple2;
import org.junit.Test;

import static com.jnape.palatable.lambda.iterables.ZippingIterable.zip;
import static com.jnape.palatable.lambda.iterables.ZippingIterable.zipWith;
import static com.jnape.palatable.lambda.tuples.Tuple2.tuple;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

public class ZippingIterableTest {

    @Test
    public void zipsTwoIterablesTogetherWithFunction() {
        Iterable<Integer> oneThroughFive = asList(1, 2, 3, 4, 5);
        Iterable<Integer> sixThroughTen = asList(6, 7, 8, 9, 10);

        DyadicFunction<Integer, Integer, Integer> add = new DyadicFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer a, Integer b) {
                return a + b;
            }
        };
        ZippingIterable<Integer, Integer, Integer> sums = zipWith(add, oneThroughFive, sixThroughTen);

        assertThat(sums, iterates(7, 9, 11, 13, 15));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void usesTuplingFunctionByDefault() {
        Iterable<String> names = asList("Angie", "Bob", "Chelsea");
        Iterable<Integer> ages = asList(24, 18, 37);

        ZippingIterable<String, Integer, Tuple2<String, Integer>> namesAndAges = zip(names, ages);
        assertThat(namesAndAges, iterates(tuple("Angie", 24), tuple("Bob", 18), tuple("Chelsea", 37)));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void zipsAsymmetricallySizedIterables() {
        Iterable<String> men = asList("Jack", "Sonny");
        Iterable<String> women = asList("Jill", "Cher", "Madonna");

        ZippingIterable<String, String, Tuple2<String, String>> couples = zip(men, women);
        assertThat(couples, iterates(tuple("Jack", "Jill"), tuple("Sonny", "Cher")));
    }
}
