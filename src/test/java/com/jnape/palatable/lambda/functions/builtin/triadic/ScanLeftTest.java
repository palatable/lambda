package com.jnape.palatable.lambda.functions.builtin.triadic;

import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.EmptyIterableSupport;
import testsupport.traits.FiniteIteration;
import testsupport.traits.Laziness;

import java.util.Collections;

import static com.jnape.palatable.lambda.functions.builtin.triadic.ScanLeft.scanLeft;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class ScanLeftTest {

    @TestTraits({EmptyIterableSupport.class, FiniteIteration.class, Laziness.class})
    public MonadicFunction<Iterable<Object>, Iterable<Object>> createTestSubject() {
        return scanLeft((x, y) -> x, new Object());
    }

    @Test
    public void buildsUpContinuationOfIncrementalAccumulations() {
        assertThat(
                scanLeft((x, y) -> x + y, 0, asList(1, 2, 3, 4, 5)),
                iterates(0, 1, 3, 6, 10, 15)
        );
    }

    @Test
    public void initialAccumulationIsOnlyResultIfEmptyIterable() {
        assertThat(
                scanLeft((x, y) -> x + y, 0, Collections.<Integer>emptyList()),
                iterates(0)
        );
    }
}
