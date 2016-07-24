package com.jnape.palatable.lambda.lens.lenses;

import com.jnape.palatable.lambda.lens.Lens;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static com.jnape.palatable.lambda.lens.functions.Set.set;
import static com.jnape.palatable.lambda.lens.functions.View.view;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public class CollectionLensTest {

    private List<String> xs;

    @Before
    public void setUp() {
        xs = new ArrayList<String>() {{
            add("foo");
            add("bar");
            add("baz");
        }};
    }

    @Test
    public void asCopyUsesMappingFunctionToFocusOnCollectionThroughCopy() {
        Lens.Simple<List<String>, List<String>> asCopy = CollectionLens.asCopy(ArrayList::new);

        assertEquals(xs, view(asCopy, xs));
        assertNotSame(xs, view(asCopy, xs));

        List<String> updatedList = asList("foo", "bar");
        assertSame(updatedList, set(asCopy, updatedList, xs));
    }

    @Test
    public void asSetFocusesOnCollectionAsSet() {
        Lens.Simple<List<String>, Set<String>> asSet = CollectionLens.asSet();

        assertEquals(new HashSet<>(xs), view(asSet, xs));
        assertEquals(singleton("foo"), view(asSet, asList("foo", "foo")));
        assertEquals(emptySet(), view(asSet, emptyList()));

        assertEquals(asList("foo", "bar"), set(asSet, new HashSet<>(asList("foo", "bar")), xs));
        assertEquals(asList("foo", "foo", "bar"),
                     set(asSet,
                         new HashSet<>(asList("foo", "bar")),
                         new ArrayList<>(asList("foo", "foo", "bar", "baz"))));
        assertEquals(emptyList(), set(asSet, emptySet(), xs));
        assertEquals(emptyList(), set(asSet, singleton("foo"), emptyList()));
    }

    @Test
    public void asStreamFocusesOnCollectionAsStream() {
        Lens.Simple<List<String>, Stream<String>> asStream = CollectionLens.asStream();

        assertEquals(xs, view(asStream, xs).collect(toList()));
        assertEquals(asList("foo", "bar"), set(asStream, Stream.of("foo", "bar"), xs));
    }
}