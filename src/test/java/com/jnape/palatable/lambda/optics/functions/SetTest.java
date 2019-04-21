package com.jnape.palatable.lambda.optics.functions;

import com.jnape.palatable.lambda.optics.Lens;
import org.junit.Test;

import java.util.List;

import static com.jnape.palatable.lambda.optics.Lens.lens;
import static com.jnape.palatable.lambda.optics.functions.Set.set;
import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static org.junit.Assert.assertEquals;

public class SetTest {

    @Test
    public void updatesWithLensAndNewValue() {
        Lens<List<String>, java.util.Set<Integer>, String, Integer> lens = lens(xs -> xs.get(0), (xs, i) -> singleton(i));
        assertEquals(singleton(5), set(lens, 5, asList("a", "aa", "aaa")));
    }
}