package com.jnape.palatable.lambda.functions.builtin.fn2;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Until.until;
import static com.jnape.palatable.lambda.io.IO.io;
import static org.junit.Assert.assertEquals;

public class UntilTest {

    @Test
    public void repeatedlyExecutesUntilPredicateMatches() {
        AtomicInteger counter = new AtomicInteger(0);
        assertEquals((Integer) 10, until(x -> x == 10, io(counter::getAndIncrement)).unsafePerformIO());
    }

    @Test
    public void predicateThatImmediatelyMatchesDoesNotChangeIO() {
        assertEquals((Integer) 0, until(constantly(true), io(0)).unsafePerformIO());
    }
}