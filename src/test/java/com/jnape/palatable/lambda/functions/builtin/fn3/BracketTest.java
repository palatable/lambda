package com.jnape.palatable.lambda.functions.builtin.fn3;

import com.jnape.palatable.lambda.io.IO;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn3.Bracket.bracket;
import static com.jnape.palatable.lambda.io.IO.io;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static testsupport.matchers.IOMatcher.throwsException;
import static testsupport.matchers.IOMatcher.yieldsValue;

public class BracketTest {

    private AtomicInteger count;

    @Before
    public void setUp() {
        count = new AtomicInteger(0);
    }

    @Test
    public void cleanupHappyPath() {
        IO<Integer> hashIO = bracket(io(() -> count), c -> io(c::incrementAndGet), c -> io(c::hashCode));

        assertEquals(0, count.get());
        assertThat(hashIO, yieldsValue(equalTo(count.hashCode())));
        assertEquals(1, count.get());
    }

    @Test
    public void cleanupSadPath() {
        IllegalStateException thrown = new IllegalStateException("kaboom");
        IO<Integer>           hashIO = bracket(io(count), c -> io(c::incrementAndGet), c -> io(() -> {throw thrown;}));

        assertThat(hashIO, throwsException(equalTo(thrown)));
        assertEquals(1, count.get());
    }

    @Test
    public void cleanupOnlyRunsIfInitialIORuns() {
        IllegalStateException thrown = new IllegalStateException("kaboom");
        IO<Integer> hashIO = bracket(io(() -> {throw thrown;}),
                                     constantly(io(count::incrementAndGet)),
                                     constantly(io(count::incrementAndGet)));

        assertThat(hashIO, throwsException(equalTo(thrown)));
        assertEquals(0, count.get());
    }

    @Test
    public void errorsInCleanupAreAddedToBodyErrors() {
        IllegalStateException bodyError    = new IllegalStateException("kaboom");
        IllegalStateException cleanupError = new IllegalStateException("KABOOM");
        IO<Integer> hashIO = bracket(io(count),
                                     constantly(io(() -> {throw cleanupError;})),
                                     constantly(io(() -> {throw bodyError;})));

        assertThat(hashIO, throwsException(equalTo(bodyError)));
        assertArrayEquals(new Throwable[]{cleanupError}, bodyError.getSuppressed());
    }
}