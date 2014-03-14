package com.jnape.palatable.lambda.functions.builtin.dyadic;

import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.EmptyIterableSupport;
import testsupport.traits.FiniteIteration;
import testsupport.traits.ImmutableIteration;
import testsupport.traits.Laziness;

import static com.jnape.palatable.lambda.functions.builtin.dyadic.Map.map;
import static com.jnape.palatable.lambda.functions.builtin.monadic.Identity.id;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class MapTest {

    @TestTraits({Laziness.class, EmptyIterableSupport.class, FiniteIteration.class, ImmutableIteration.class})
    public MonadicFunction<? extends Iterable, ?> createTraitsTestSubject() {
        return map(id());
    }

    @Test
    public void mapsInputsIntoOutputs() {
        MonadicFunction<String, Integer> length = new MonadicFunction<String, Integer>() {
            @Override
            public Integer apply(String string) {
                return string.length();
            }
        };
        assertThat(
                map(length, asList("one", "two", "three")),
                iterates(3, 3, 5)
        );
    }
}
