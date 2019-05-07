package com.jnape.palatable.lambda.functions.specialized;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

public class SideEffectTest {

    @Test
    public void fromRunnable() throws Throwable {
        AtomicInteger counter    = new AtomicInteger(0);
        Runnable      runnable   = counter::incrementAndGet;
        SideEffect    sideEffect = SideEffect.fromRunnable(runnable);
        sideEffect.Ω();
        assertEquals(1, counter.get());
    }

    @Test
    public void toRunnable() {
        AtomicInteger counter    = new AtomicInteger(0);
        SideEffect    sideEffect = counter::incrementAndGet;
        Runnable      runnable   = sideEffect.toRunnable();
        runnable.run();
        assertEquals(1, counter.get());
    }
}