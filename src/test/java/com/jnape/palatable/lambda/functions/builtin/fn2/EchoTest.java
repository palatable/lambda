package com.jnape.palatable.lambda.functions.builtin.fn2;

import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Drop.drop;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Echo.echo;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Iterate.iterate;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Take.take;
import static org.junit.Assert.assertThat;
import static testsupport.Constants.STACK_EXPLODING_NUMBER;
import static testsupport.matchers.IterableMatcher.iterates;

public class EchoTest {

    @Test
    public void echoesEachElement() {
        Iterable<Integer> numbers = iterate(x -> x + 1, 1);
        assertThat(take(9, echo(3, numbers)), iterates(1, 1, 1, 2, 2, 2, 3, 3, 3));
    }

    @Test
    public void echoesEachElementForever() {
        Iterable<Integer> numbers = iterate(x -> x + 1, 1);
        assertThat(take(3, drop(3 * (STACK_EXPLODING_NUMBER - 1), echo(3, numbers))),
                   iterates(STACK_EXPLODING_NUMBER, STACK_EXPLODING_NUMBER, STACK_EXPLODING_NUMBER));
    }
}