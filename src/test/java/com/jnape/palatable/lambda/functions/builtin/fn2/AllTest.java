package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.EmptyIterableSupport;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Repeat.repeat;
import static com.jnape.palatable.lambda.functions.builtin.fn2.All.all;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(Traits.class)
public class AllTest {

    private static final Fn1<? super Number, Boolean> EVEN = x -> x.doubleValue() % 2 == 0;

    @TestTraits({EmptyIterableSupport.class})
    public Fn1<Iterable<Object>, Boolean> createTestSubject() {
        return all(constantly(true));
    }

    @Test
    public void trueIfAllElementsMatchPredicate() {
        assertThat(all(EVEN, asList(2, 4, 6, 8)), is(true));
    }

    @Test
    public void falseIfAnyElementsFailPredicate() {
        assertThat(all(EVEN, asList(1, 2, 4, 6, 8)), is(false));
    }

    @Test
    public void terminatesIterationImmediatelyUponPredicateFailure() {
        assertThat(all(EVEN, repeat(1)), is(false));
    }
}
