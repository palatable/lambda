package com.jnape.palatable.lambda.lens.lenses;

import com.jnape.palatable.lambda.lens.Lens;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.jnape.palatable.lambda.lens.functions.Set.set;
import static com.jnape.palatable.lambda.lens.functions.View.view;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public class ListLensTest {

    private List<String> xs;

    @Before
    public void setUp() throws Exception {
        xs = new ArrayList<String>() {{
            add("foo");
            add("bar");
            add("baz");
        }};
    }

    @Test
    public void asCopyFocusesOnListThroughCopy() {
        Lens.Simple<List<String>, List<String>> asCopy = ListLens.asCopy();

        assertEquals(xs, view(asCopy, xs));
        assertNotSame(xs, view(asCopy, xs));

        List<String> update = asList("foo", "bar", "baz", "quux");
        assertSame(update, set(asCopy, update, xs));
    }

    @Test
    public void atFocusesOnElementAtIndex() {
        Lens<List<String>, List<String>, Optional<String>, String> at0 = ListLens.at(0);

        assertEquals(Optional.of("foo"), view(at0, xs));
        assertEquals(Optional.empty(), view(at0, emptyList()));
        assertEquals(asList("quux", "bar", "baz"), set(at0, "quux", xs));
        assertEquals(emptyList(), set(at0, "quux", emptyList()));
    }
}