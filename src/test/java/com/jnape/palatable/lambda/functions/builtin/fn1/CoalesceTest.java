package com.jnape.palatable.lambda.functions.builtin.fn1;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Coalesce.coalesce;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.EitherMatcher.isLeftThat;
import static testsupport.matchers.EitherMatcher.isRightThat;
import static testsupport.matchers.IterableMatcher.isEmpty;
import static testsupport.matchers.IterableMatcher.iterates;

public class CoalesceTest {

    @Test
    public void empty() {
        assertThat(coalesce(emptyList()), isRightThat(isEmpty()));
    }

    @Test
    public void allRights() {
        assertThat(coalesce(asList(right(1), right(2), right(3))), isRightThat(iterates(1, 2, 3)));
    }

    @Test
    public void allLefts() {
        assertThat(coalesce(asList(left("foo"), left("bar"), left("baz"))), isLeftThat(iterates("foo", "bar", "baz")));
    }

    @Test
    public void someRightsAndLefts() {
        assertThat(coalesce(asList(right(1), left("foo"), right(2), left("bar"), right(3), left("baz"))),
                   isLeftThat(iterates("foo", "bar", "baz")));
    }
}