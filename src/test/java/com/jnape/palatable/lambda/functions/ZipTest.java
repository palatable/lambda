package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.MonadicFunction;
import com.jnape.palatable.lambda.staticfactory.IterableFactory;
import com.jnape.palatable.lambda.tuples.Tuple2;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.FiniteIteration;
import testsupport.traits.ImmutableIteration;
import testsupport.traits.Laziness;

import static com.jnape.palatable.lambda.functions.Zip.zip;
import static com.jnape.palatable.lambda.tuples.Tuple2.tuple;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class ZipTest {

    @TestTraits({Laziness.class, FiniteIteration.class, ImmutableIteration.class})
    public MonadicFunction<Iterable<Object>, Iterable<Tuple2<Object, Object>>> createTestSubject() {
        return zip(IterableFactory.<Object>iterable(1, 2, 3));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void zipsTwoIterablesTogether() {
        Iterable<Integer> odds = asList(1, 3, 5);
        Iterable<Integer> evens = asList(2, 4, 6);

        Iterable<Tuple2<Integer, Integer>> numbers = zip(odds, evens);
        assertThat(numbers, iterates(tuple(1, 2), tuple(3, 4), tuple(5, 6)));
    }
}
