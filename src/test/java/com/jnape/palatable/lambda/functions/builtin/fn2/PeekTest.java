package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.Maybe;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Peek.peek;
import static org.junit.Assert.assertEquals;

public class PeekTest {

    @Test
    public void appliesConsumerToCarrierValue() {
        AtomicInteger counter = new AtomicInteger(0);
        Maybe<String> maybeString = just("foo");
        assertEquals(maybeString, peek(x -> counter.incrementAndGet(), maybeString));
        assertEquals(1, counter.get());
    }

    @Test
    public void onlyAppliesIfFmapWould() {
        AtomicInteger counter = new AtomicInteger(0);
        Maybe.<String>nothing().fmap(__ -> counter.incrementAndGet());
        assertEquals(0, counter.get());
    }
}