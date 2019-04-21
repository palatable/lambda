package com.jnape.palatable.lambda.optics.functions;

import com.jnape.palatable.lambda.optics.Lens;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static com.jnape.palatable.lambda.optics.Lens.lens;
import static com.jnape.palatable.lambda.optics.functions.View.view;
import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static org.junit.Assert.assertEquals;

public class ViewTest {

    @Test
    public void viewsSubPartWithLens() {
        Lens<List<String>, Set<Integer>, String, Integer> lens = lens(xs -> xs.get(0), (xs, i) -> singleton(i));
        assertEquals("foo", view(lens, asList("foo", "bar", "baz")));
    }
}