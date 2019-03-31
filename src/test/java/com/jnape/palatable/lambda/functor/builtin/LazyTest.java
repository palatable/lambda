package com.jnape.palatable.lambda.functor.builtin;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn3.Times.times;
import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static testsupport.Constants.STACK_EXPLODING_NUMBER;

public class LazyTest {

    @Test
    public void valueExtraction() {
        assertEquals("foo", lazy("foo").value());
        assertEquals("foo", lazy(() -> "foo").value());
    }

    @Test
    public void lazyEvaluation() {
        AtomicBoolean invoked = new AtomicBoolean(false);
        Lazy<Integer> lazy = lazy(0).flatMap(x -> {
            invoked.set(true);
            return lazy(x + 1);
        });
        assertFalse(invoked.get());
        assertEquals((Integer) 1, lazy.value());
        assertTrue(invoked.get());
    }

    @Test
    public void linearStackSafety() {
        assertEquals(STACK_EXPLODING_NUMBER,
                     times(STACK_EXPLODING_NUMBER, f -> f.fmap(x -> x + 1), lazy(0)).value());
    }

    @Test
    public void recursiveStackSafety() {
        assertEquals(STACK_EXPLODING_NUMBER,
                     new Function<Lazy<Integer>, Lazy<Integer>>() {
                         @Override
                         public Lazy<Integer> apply(Lazy<Integer> lazy) {
                             return lazy.flatMap(x -> x < STACK_EXPLODING_NUMBER ? apply(lazy(x + 1)) : lazy(x));
                         }
                     }.apply(lazy(0))
                             .value());
    }
}