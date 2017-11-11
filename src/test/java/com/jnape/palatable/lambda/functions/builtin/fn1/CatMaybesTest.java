package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.CatMaybes.catMaybes;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Repeat.repeat;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Take.take;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.isEmpty;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class CatMaybesTest {

    @Test
    public void empty() {
        assertThat(catMaybes(emptyList()), isEmpty());
    }

    @Test
    public void onlyNothingsIsEquivalentToEmpty() {
        assertThat(catMaybes(asList(nothing(), nothing(), nothing())), isEmpty());
    }

    @Test
    public void nonEmpty() {
        assertThat(catMaybes(asList(nothing(), just(1), just(2), nothing(), just(3))), iterates(1, 2, 3));
    }

    @Test
    public void infiniteIterableSupport() {
        assertThat(take(3, catMaybes(repeat(just(1)))), iterates(1, 1, 1));
    }
}