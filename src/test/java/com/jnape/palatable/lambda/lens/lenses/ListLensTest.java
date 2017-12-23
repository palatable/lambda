package com.jnape.palatable.lambda.lens.lenses;

import com.jnape.palatable.lambda.lens.Lens;
import org.junit.Test;

import java.util.List;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.lens.functions.Set.set;
import static com.jnape.palatable.lambda.lens.functions.View.view;
import static com.jnape.palatable.lambda.lens.lenses.ListLens.asCopy;
import static com.jnape.palatable.lambda.lens.lenses.ListLens.elementAt;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static testsupport.assertion.LensAssert.assertLensLawfulness;

public class ListLensTest {

    @Test
    public void asCopyFocusesOnListThroughCopy() {
        assertLensLawfulness(asCopy(),
                             asList(emptyList(), asList("foo", "bar", "baz")),
                             asList(emptyList(), asList("foo", "bar", "baz", "quux")));
    }

    @Test
    public void elementAtFocusesOnElementAtIndex() {
        assertLensLawfulness(elementAt(0),
                             asList(emptyList(), asList("foo", "bar", "baz"), asList("quux", "bar", "baz")),
                             asList(nothing(), just("foo"), just("quux")));
    }

    @Test
    public void elementAtWithDefaultValueFocusesOnElementAtIndex() {
        Lens<List<String>, List<String>, String, String> at0 = ListLens.elementAt(0, "missing");

        assertEquals("foo", view(at0, asList("foo", "bar", "baz")));
        assertEquals("missing", view(at0, emptyList()));
        assertEquals(asList("quux", "bar", "baz"), set(at0, "quux", asList("foo", "bar", "baz")));
        assertEquals(singletonList("quux"), set(at0, "quux", emptyList()));
    }
}