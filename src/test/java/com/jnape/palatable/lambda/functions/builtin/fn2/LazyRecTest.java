package com.jnape.palatable.lambda.functions.builtin.fn2;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.jnape.palatable.lambda.functions.builtin.fn2.LazyRec.lazyRec;
import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static testsupport.Constants.STACK_EXPLODING_NUMBER;

public class LazyRecTest {

    @Test
    public void recursivelyComputesValue() {
        assertEquals(STACK_EXPLODING_NUMBER,
                     LazyRec.<Integer, Integer>lazyRec((f, x) -> x < STACK_EXPLODING_NUMBER
                                                                 ? f.apply(x + 1)
                                                                 : lazy(x),
                                                       0)
                             .value());
    }

    @Test
    public void defersAllComputationUntilForced() {
        AtomicBoolean invoked = new AtomicBoolean(false);
        lazyRec((f, x) -> {
                    invoked.set(true);
                    return lazy(x);
                },
                0);

        assertFalse(invoked.get());
    }

}