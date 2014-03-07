package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.DyadicFunction;
import com.jnape.palatable.lambda.MonadicFunction;
import com.jnape.palatable.lambda.tuples.Tuple2;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.FiniteIteration;
import testsupport.traits.ImmutableIteration;
import testsupport.traits.Laziness;

import static com.jnape.palatable.lambda.functions.Zip.zip;
import static com.jnape.palatable.lambda.functions.ZipWith.zipWith;
import static com.jnape.palatable.lambda.staticfactory.IterableFactory.iterable;
import static com.jnape.palatable.lambda.tuples.Tuple2.tuple;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class ZipWithTest {

    @TestTraits({Laziness.class, FiniteIteration.class, ImmutableIteration.class})
    public MonadicFunction<Iterable<Object>, Iterable<Object>> createTestSubject() {
        return zipWith(new DyadicFunction<Object, Object, Object>() {
            @Override
            public Object apply(Object o, Object o2) {
                return new Object();
            }
        }, iterable(1, 2, 3));
    }

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
        Iterable<Integer> sums = ZipWith.zipWith(add, oneThroughFive, sixThroughTen);

        assertThat(sums, iterates(7, 9, 11, 13, 15));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void zipsAsymmetricallySizedIterables() {
        Iterable<String> men = asList("Jack", "Sonny");
        Iterable<String> women = asList("Jill", "Cher", "Madonna");

        Iterable<Tuple2<String, String>> couples = zip(men, women);
        assertThat(couples, iterates(tuple("Jack", "Jill"), tuple("Sonny", "Cher")));
    }
}
