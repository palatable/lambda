package com.jnape.palatable.lambda.continuation;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.util.stream.Stream;

import static com.jnape.palatable.lambda.continuation.Continuations.continuing;
import static java.lang.reflect.Modifier.isPrivate;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static testsupport.matchers.IterableMatcher.iterates;

public class ContinuationsTest {

    @Test
    public void cannotBeInstantiated() {
        Constructor<?>[] constructors = Continuations.class.getDeclaredConstructors();
        assertThat(constructors.length, is(1));
        assertTrue(isPrivate(constructors[0].getModifiers()));
    }

    @Test
    public void continuingVarargs() {
        assertThat(continuing(1, 2, 3), iterates(1, 2, 3));
    }

    @Test
    public void continuingStreams() {
        Stream<Integer> stream = Stream.<Integer>builder()
                .add(1)
                .add(2)
                .add(3)
                .build();
        assertThat(continuing(stream), iterates(1, 2, 3));
    }

    @Test
    public void continuingIterables() {
        assertThat(continuing(asList(1, 2, 3)), iterates(1, 2, 3));
    }

    @Test
    public void continuingIterators() {
        assertThat(continuing(asList(1, 2, 3).iterator()), iterates(1, 2, 3));
    }
}