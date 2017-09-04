package com.jnape.palatable.lambda.lens.lenses;

import com.jnape.palatable.lambda.lens.Lens;
import org.junit.Test;

import java.util.Optional;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;
import static com.jnape.palatable.lambda.lens.functions.Over.over;
import static com.jnape.palatable.lambda.lens.functions.Set.set;
import static com.jnape.palatable.lambda.lens.functions.View.view;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.isEmpty;
import static testsupport.matchers.IterableMatcher.iterates;

public class IterableLensTest {

    @Test
    public void head() {
        Lens<Iterable<Integer>, Iterable<Integer>, Optional<Integer>, Integer> head = IterableLens.head();

        assertEquals(Optional.of(1), view(head, asList(1, 2, 3)));
        assertEquals(Optional.empty(), view(head, emptyList()));

        assertThat(set(head, 1, emptyList()), iterates(1));
        assertThat(set(head, 1, asList(2, 2, 3)), iterates(1, 2, 3));

        assertThat(over(head, x -> x.orElse(0) + 1, emptyList()), iterates(1));
        assertThat(over(head, x -> x.orElse(0) + 1, asList(1, 2, 3)), iterates(2, 2, 3));
    }

    @Test
    public void tail() {
        Lens.Simple<Iterable<Integer>, Iterable<Integer>> tail = IterableLens.tail();

        assertThat(view(tail, asList(1, 2, 3)), iterates(2, 3));
        assertThat(view(tail, emptyList()), isEmpty());

        assertThat(set(tail, asList(2, 3), singletonList(1)), iterates(1, 2, 3));
        assertThat(set(tail, emptyList(), asList(1, 2, 3)), iterates(1));
        assertThat(set(tail, asList(1, 2, 3), emptyList()), iterates(1, 2, 3));
        assertThat(set(tail, emptyList(), emptyList()), isEmpty());

        assertThat(over(tail, map(x -> x + 1), emptyList()), isEmpty());
        assertThat(over(tail, map(x -> x + 1), asList(1, 2, 3)), iterates(1, 3, 4));
    }
}