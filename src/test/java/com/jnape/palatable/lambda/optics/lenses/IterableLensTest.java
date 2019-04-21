package com.jnape.palatable.lambda.optics.lenses;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.optics.Iso;
import com.jnape.palatable.lambda.optics.Lens;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;
import static com.jnape.palatable.lambda.optics.Iso.simpleIso;
import static com.jnape.palatable.lambda.optics.functions.Over.over;
import static com.jnape.palatable.lambda.optics.functions.Set.set;
import static com.jnape.palatable.lambda.optics.functions.View.view;
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
        Lens.Simple<Iterable<Integer>, Maybe<Integer>> head = IterableLens.head();

        assertEquals(just(1), view(head, asList(1, 2, 3)));
        assertEquals(nothing(), view(head, emptyList()));

        assertThat(set(head, just(1), emptyList()), iterates(1));
        assertThat(set(head, nothing(), emptyList()), isEmpty());
        assertThat(set(head, just(1), asList(2, 2, 3)), iterates(1, 2, 3));
        assertThat(set(head, nothing(), asList(2, 2, 3)), iterates(2, 3));

        assertThat(over(head, maybeX -> maybeX.fmap(x -> x + 1), emptyList()), isEmpty());
        assertThat(over(head, maybeX -> maybeX.fmap(x -> x + 1), asList(1, 2, 3)), iterates(2, 2, 3));
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

    @Test
    public void mapping() {
        Iso.Simple<Iterable<String>, Iterable<Integer>> iso = IterableLens.mapping(simpleIso(Integer::parseInt, Object::toString));

        assertThat(view(iso, emptyList()), isEmpty());
        assertThat(view(iso, singletonList("1")), iterates(1));
        assertThat(view(iso, asList("1", "2", "3")), iterates(1, 2, 3));

        assertThat(set(iso, emptyList(), emptyList()), isEmpty());
        assertThat(set(iso, singletonList(1), emptyList()), iterates("1"));
        assertThat(set(iso, singletonList(2), singletonList("1")), iterates("2"));
        assertThat(set(iso, asList(1, 2, 3), singletonList("1")), iterates("1", "2", "3"));
        assertThat(set(iso, emptyList(), singletonList("1")), isEmpty());
    }
}