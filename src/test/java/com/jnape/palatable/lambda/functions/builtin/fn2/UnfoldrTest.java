package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.ImmutableIteration;
import testsupport.traits.InfiniteIteration;
import testsupport.traits.Laziness;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Take.take;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Unfoldr.unfoldr;
import static org.hamcrest.MatcherAssert.assertThat;
import static testsupport.matchers.IterableMatcher.isEmpty;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class UnfoldrTest {

    @TestTraits({Laziness.class, InfiniteIteration.class, ImmutableIteration.class})
    public Fn1<? extends Iterable<?>, ? extends Iterable<?>> createTestSubject() {
        return unfoldr(x -> just(tuple(x, x)));
    }

    @Test
    public void iteratesIterableFromSeedValueAndSuccessiveFunctionApplications() {
        assertThat(take(5, unfoldr(x -> just(tuple(x, x + 1)), 0)), iterates(0, 1, 2, 3, 4));
    }

    @Test
    public void emptyIteration() {
        assertThat(unfoldr(constantly(nothing()), 1), isEmpty());
    }
}
