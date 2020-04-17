package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.EmptyIterableSupport;
import testsupport.traits.FiniteIteration;
import testsupport.traits.ImmutableIteration;
import testsupport.traits.InfiniteIterableSupport;
import testsupport.traits.Laziness;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class MapTest {

    @TestTraits({Laziness.class, EmptyIterableSupport.class, InfiniteIterableSupport.class, FiniteIteration.class, ImmutableIteration.class})
    public Fn1<? extends Iterable<?>, ?> createTraitsTestSubject() {
        return map(id());
    }

    @Test
    public void mapsInputsIntoOutputs() {
        Fn1<String, Integer> length = String::length;
        assertThat(
                map(length, asList("one", "two", "three")),
                iterates(3, 3, 5)
        );
    }
}
