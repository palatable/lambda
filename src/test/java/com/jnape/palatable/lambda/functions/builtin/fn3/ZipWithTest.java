package com.jnape.palatable.lambda.functions.builtin.fn3;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.FiniteIteration;
import testsupport.traits.ImmutableIteration;
import testsupport.traits.Laziness;

import java.util.function.BiFunction;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Zip.zip;
import static com.jnape.palatable.lambda.functions.builtin.fn3.ZipWith.zipWith;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class ZipWithTest {

    @TestTraits({Laziness.class, FiniteIteration.class, ImmutableIteration.class})
    public Fn1<Iterable<Object>, Iterable<Object>> createTestSubject() {
        return zipWith((o, o2) -> new Object(), asList(1, 2, 3));
    }

    @Test
    public void zipsTwoIterablesTogetherWithFunction() {
        Iterable<Integer> oneThroughFive = asList(1, 2, 3, 4, 5);
        Iterable<Integer> sixThroughTen = asList(6, 7, 8, 9, 10);

        BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
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
