package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.EmptyIterableSupport;

import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Repeat.repeat;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Any.any;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(Traits.class)
public class AnyTest {

    public static final Function<? super Integer, Boolean> EVEN = x -> x % 2 == 0;

    @TestTraits({EmptyIterableSupport.class})
    public Fn1<Iterable<Object>, Boolean> createTestSubject() {
        return any(constantly(true));
    }

    @Test
    public void trueIfAnyElementsMatchPredicate() {
        assertThat(any(EVEN, asList(1, 2)), is(true));
    }

    @Test
    public void falseIfNoElementsMatchPredicate() {
        assertThat(any(EVEN, asList(1, 3, 5)), is(false));
    }

    @Test
    public void terminatesIterationImmediatelyUponPredicateSuccess() {
        assertThat(any(EVEN, repeat(2)), is(true));
    }
}
