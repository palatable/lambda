package com.jnape.palatable.lambda.iterators;

import com.jnape.palatable.lambda.functions.MonadicFunction;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MappingIteratorTest {

    @Test
    public void nextProducesMappedResult() {
        MonadicFunction<String, Integer> stringToLength = new MonadicFunction<String, Integer>() {
            @Override
            public Integer apply(String s) {
                return s.length();
            }
        };
        List<String> words = asList("foo", "bar");
        MappingIterator<String, Integer> mappingIterator = new MappingIterator<String, Integer>(stringToLength, words.iterator());

        assertThat(mappingIterator.next(), is(3));
    }
}
