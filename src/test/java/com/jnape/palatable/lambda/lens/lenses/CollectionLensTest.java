package com.jnape.palatable.lambda.lens.lenses;

import com.jnape.palatable.lambda.lens.Lens;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import static com.jnape.palatable.lambda.lens.functions.Set.set;
import static com.jnape.palatable.lambda.lens.functions.View.view;
import static com.jnape.palatable.lambda.lens.lenses.CollectionLens.asCopy;
import static com.jnape.palatable.lambda.lens.lenses.CollectionLens.asSet;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static testsupport.assertion.LensAssert.assertLensLawfulness;

public class CollectionLensTest {

    @Test
    public void asCopyUsesMappingFunctionToFocusOnCollectionThroughCopy() {
        assertLensLawfulness(asCopy(ArrayList::new),
                             asList(emptyList(), asList("foo", "bar", "baz")),
                             asList(emptyList(), asList("foo", "bar", "baz")));
    }

    @Test
    public void asSetFocusesOnCollectionAsSet() {
        assertLensLawfulness(asSet(ArrayList::new),
                             asList(emptyList(), asList("foo", "bar", "baz"), asList("foo", "foo")),
                             asList(emptySet(), singleton("foo"), new HashSet<>(asList("foo", "bar", "baz")), new HashSet<>(asList("foo", "bar"))));
    }

    @Test
    public void asStreamFocusesOnCollectionAsStream() {
        Lens.Simple<List<String>, Stream<String>> asStream = CollectionLens.asStream(ArrayList::new);

        assertEquals(asList("foo", "bar", "baz"), view(asStream, asList("foo", "bar", "baz")).collect(toList()));
        assertEquals(asList("foo", "bar"), set(asStream, Stream.of("foo", "bar"), asList("foo", "bar", "baz")));
    }
}