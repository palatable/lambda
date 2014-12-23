package com.jnape.palatable.lambda.functions.builtin.dyadic;

import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.EmptyIterableSupport;

import static com.jnape.palatable.lambda.functions.builtin.dyadic.Any.any;
import static com.jnape.palatable.lambda.functions.builtin.monadic.Always.always;
import static com.jnape.palatable.lambda.functions.builtin.monadic.Repeat.repeat;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(Traits.class)
public class AnyTest {

    public static final MonadicFunction<? super Integer, Boolean> EVEN = x -> x % 2 == 0;

    @TestTraits({EmptyIterableSupport.class})
    public MonadicFunction<Iterable<Object>, Boolean> createTestSubject() {
        return any(always(true));
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
