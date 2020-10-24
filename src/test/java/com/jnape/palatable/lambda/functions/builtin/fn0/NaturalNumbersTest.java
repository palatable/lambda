package com.jnape.palatable.lambda.functions.builtin.fn0;

import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn0.NaturalNumbers.naturalNumbers;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Drop.drop;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Take.take;
import static org.junit.Assert.*;
import static testsupport.Constants.STACK_EXPLODING_NUMBER;
import static testsupport.matchers.IterableMatcher.iterates;

public class NaturalNumbersTest {
    @Test
    public void producesTheNats() {
        Iterable<Integer> numbers = naturalNumbers();
        assertThat(take(10, numbers), iterates(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
    }

    @Test
    public void producesNatsForever() {
        Iterable<Integer> numbers = naturalNumbers();
        assertThat(take(10, drop(STACK_EXPLODING_NUMBER, numbers)),
                   iterates(STACK_EXPLODING_NUMBER + 1,
                            STACK_EXPLODING_NUMBER + 2,
                            STACK_EXPLODING_NUMBER + 3,
                            STACK_EXPLODING_NUMBER + 4,
                            STACK_EXPLODING_NUMBER + 5,
                            STACK_EXPLODING_NUMBER + 6,
                            STACK_EXPLODING_NUMBER + 7,
                            STACK_EXPLODING_NUMBER + 8,
                            STACK_EXPLODING_NUMBER + 9,
                            STACK_EXPLODING_NUMBER + 10));
    }
}