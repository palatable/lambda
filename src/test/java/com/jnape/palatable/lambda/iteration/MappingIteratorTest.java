package com.jnape.palatable.lambda.iteration;

import com.jnape.palatable.lambda.functions.Fn1;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MappingIteratorTest {

    @Test
    public void nextProducesMappedResult() {
        Fn1<String, Integer> stringToLength = String::length;
        List<String> words = asList("foo", "bar");
        MappingIterator<String, Integer> mappingIterator = new MappingIterator<>(stringToLength, words.iterator());

        assertThat(mappingIterator.next(), is(3));
    }
}
