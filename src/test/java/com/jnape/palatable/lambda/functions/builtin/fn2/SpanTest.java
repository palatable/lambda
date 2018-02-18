package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.monoid.builtin.Concat;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.EmptyIterableSupport;
import testsupport.traits.FiniteIteration;
import testsupport.traits.ImmutableIteration;
import testsupport.traits.InfiniteIterableSupport;
import testsupport.traits.Laziness;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Eq.eq;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Span.span;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class SpanTest {

    @TestTraits({EmptyIterableSupport.class, InfiniteIterableSupport.class, FiniteIteration.class, ImmutableIteration.class, Laziness.class})
    public Fn1<Iterable<Integer>, Iterable<Integer>> testSubject() {
        return span(eq(0)).fmap(into(Concat::concat));
    }

    @Test
    public void splitsIterableAfterPredicateFailure() {
        Tuple2<Iterable<Integer>, Iterable<Integer>> spanned = span(eq(1), asList(1, 1, 1, 2, 3, 1));
        assertThat(spanned._1(), iterates(1, 1, 1));
        assertThat(spanned._2(), iterates(2, 3, 1));
    }
}