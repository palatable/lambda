package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.MonadicFunction;
import org.junit.Test;

import static com.jnape.palatable.lambda.builtin.monadic.Identity.id;
import static com.jnape.palatable.lambda.functions.Map.map;
import static com.jnape.palatable.lambda.staticfactory.IterableFactory.iterable;
import static org.junit.Assert.assertThat;
import static testsupport.Mocking.mockIterable;
import static testsupport.matchers.IterableMatcher.isEmpty;
import static testsupport.matchers.IterableMatcher.iterates;
import static testsupport.matchers.ZeroInvocationsMatcher.wasNeverInteractedWith;

public class MapTest {

    @Test
    public void mapsInputsIntoOutputs() {
        MonadicFunction<String, Integer> length = new MonadicFunction<String, Integer>() {
            @Override
            public Integer apply(String string) {
                return string.length();
            }
        };
        assertThat(
                map(length, iterable("one", "two", "three")),
                iterates(3, 3, 5)
        );
    }

    @Test
    @SuppressWarnings("unchecked")
    public void worksOnEmptyIterables() {
        assertThat(map(id(), iterable()), isEmpty());
    }

    @Test
    public void defersIteration() {
        Iterable<Object> iterable = mockIterable();
        map(id(), iterable);
        assertThat(iterable, wasNeverInteractedWith());
        assertThat(iterable.iterator(), wasNeverInteractedWith());
    }
}
