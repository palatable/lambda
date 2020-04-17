package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.io.IO;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static com.jnape.palatable.lambda.functions.builtin.fn2.AutoBracket.autoBracket;
import static com.jnape.palatable.lambda.io.IO.io;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static testsupport.matchers.IOMatcher.throwsException;
import static testsupport.matchers.IOMatcher.yieldsValue;

public class AutoBracketTest {

    private AtomicInteger closedCounter;
    private AutoCloseable autoCloseable;

    @Before
    public void setUp() {
        closedCounter = new AtomicInteger(0);
        autoCloseable = closedCounter::incrementAndGet;
    }

    @Test
    public void closeWhenDone() {
        IO<Integer> bracketed = autoBracket(io(autoCloseable), closeable -> io(1));

        assertEquals(0, closedCounter.get());
        assertThat(bracketed, yieldsValue(equalTo(1)));
        assertEquals(1, closedCounter.get());
    }

    @Test
    public void closeOnException() {
        RuntimeException cause = new RuntimeException();

        IO<Unit> bracketed = autoBracket(io(autoCloseable), closeable -> IO.throwing(cause));

        assertEquals(0, closedCounter.get());
        assertThat(bracketed, throwsException(equalTo(cause)));
        assertEquals(1, closedCounter.get());
    }
}