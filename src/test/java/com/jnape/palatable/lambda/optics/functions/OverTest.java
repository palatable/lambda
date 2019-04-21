package com.jnape.palatable.lambda.optics.functions;

import com.jnape.palatable.lambda.optics.Lens;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static com.jnape.palatable.lambda.optics.Lens.lens;
import static com.jnape.palatable.lambda.optics.functions.Over.over;
import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static org.junit.Assert.assertEquals;

public class OverTest {

    @Test
    public void mapsDataWithLensAndFunction() {
        Lens<List<String>, Set<Integer>, String, Integer> lens = lens(xs -> xs.get(0), (xs, i) -> singleton(i));
        assertEquals(singleton(1), over(lens, String::length, asList("a", "aa", "aaa")));
    }
}