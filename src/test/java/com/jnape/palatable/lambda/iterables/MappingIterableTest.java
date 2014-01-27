package com.jnape.palatable.lambda.iterables;

import com.jnape.palatable.lambda.MonadicFunction;
import org.junit.Test;

import static com.jnape.palatable.lambda.iterables.MappingIterable.map;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

public class MappingIterableTest {

    @Test
    public void mapsInputsIntoOutputs() {
        Iterable<String> strings = asList("one", "two", "three");
        MappingIterable<String, Integer> stringsToLengths = map(new MonadicFunction<String, Integer>() {
            @Override
            public Integer apply(String string) {
                return string.length();
            }
        }, strings);

        assertThat(stringsToLengths, iterates(3, 3, 5));
    }
}
