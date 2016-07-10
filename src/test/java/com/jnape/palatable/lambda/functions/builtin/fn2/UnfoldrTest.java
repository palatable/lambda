package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.ImmutableIteration;
import testsupport.traits.InfiniteIteration;
import testsupport.traits.Laziness;

import java.util.Optional;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Take.take;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Unfoldr.unfoldr;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class UnfoldrTest {

    @TestTraits({Laziness.class, InfiniteIteration.class, ImmutableIteration.class})
    public Fn1<? extends Iterable, ? extends Iterable> createTestSubject() {
        return unfoldr(x -> Optional.of(tuple(x, x)));
    }

    @Test
    public void iteratesIterableFromSeedValueAndSuccessiveFunctionApplications() {
        assertThat(take(5, unfoldr(x -> Optional.of(tuple(x, x + 1)), 0)), iterates(0, 1, 2, 3, 4));
    }
}
