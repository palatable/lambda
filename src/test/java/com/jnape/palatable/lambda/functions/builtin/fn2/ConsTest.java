package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.EmptyIterableSupport;
import testsupport.traits.FiniteIteration;
import testsupport.traits.ImmutableIteration;
import testsupport.traits.Laziness;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class ConsTest {

    @TestTraits({Laziness.class, ImmutableIteration.class, FiniteIteration.class, EmptyIterableSupport.class})
    public Fn1<Iterable<Object>, Iterable<Object>> createTestSubject() {
        return cons(0);
    }

    @Test
    public void consingElementToHeadOfIterable() {
        assertThat(cons(0, asList(1, 2, 3)), iterates(0, 1, 2, 3));
    }

    @Test
    public void consingToEmptyIterable() {
        assertThat(cons("foo", emptyList()), iterates("foo"));
    }
}
