package com.jnape.palatable.lambda.functions.builtin.fn3;

import com.jnape.palatable.lambda.functions.Fn1;
import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn3.Times.times;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TimesTest {

    @Test
    public void accumulatesFunctionNTimes() {
        Fn1<Integer, Integer> inc = x -> x + 1;
        assertEquals((Integer) 3, times(3, inc).apply(0));
    }

    @Test
    public void zeroIsMeansReturnTheInputBack() {
        Fn1<Integer, Integer> inc = x -> x + 1;
        assertEquals((Integer) 0, times(0, inc).apply(0));
    }

    @Test
    public void lessThanZeroIsIllegal() {
        assertThrows(
            "n must not be less than 0",
            IllegalStateException.class,
            () -> times(-1, id(), 1)
        );
    }

    @Test
    public void stackSafety() {
        int stackBlowingNumber = 50000;
        assertEquals((Integer) stackBlowingNumber, times(stackBlowingNumber, x -> x + 1, 0));
    }
}