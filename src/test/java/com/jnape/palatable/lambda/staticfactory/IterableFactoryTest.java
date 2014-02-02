package com.jnape.palatable.lambda.staticfactory;

import org.junit.Test;

import java.util.Iterator;

import static com.jnape.palatable.lambda.staticfactory.IterableFactory.iterable;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

public class IterableFactoryTest {

    @Test
    public void wrapsIterator() {
        Iterator<String> iterator = asList("a", "b", "c").iterator();
        assertThat(iterable(iterator), iterates("a", "b", "c"));
    }

    @Test
    public void acceptsVarArgsToo() {
        assertThat(iterable(1, 2, 3), iterates(1, 2, 3));
    }
}
