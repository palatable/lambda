package com.jnape.palatable.lambda.functions.builtin.fn3;

import com.jnape.palatable.lambda.io.IO;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static com.jnape.palatable.lambda.functions.builtin.fn3.Bracket.bracket;
import static com.jnape.palatable.lambda.io.IO.io;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
        assertEquals((Integer) count.hashCode(), hashIO.unsafePerformIO());
        assertEquals(1, count.get());
    }

    @Test
    public void cleanupSadPath() {
        IllegalStateException thrown = new IllegalStateException("kaboom");
        IO<Integer>           hashIO = bracket(io(count), c -> io(c::incrementAndGet), c -> io(() -> {throw thrown;}));

        try {
            hashIO.unsafePerformIO();
            fail("Expected exception to be raised");
        } catch (IllegalStateException actual) {
            assertEquals(thrown, actual);
            assertEquals(1, count.get());
        }
    }

    @Test
    public void cleanupOnlyRunsIfInitialIORuns() {
        IllegalStateException thrown = new IllegalStateException("kaboom");
        IO<Integer> hashIO = bracket(io(() -> {throw thrown;}),
                                     __ -> io(count::incrementAndGet),
                                     __ -> io(count::incrementAndGet));
        try {
            hashIO.unsafePerformIO();
            fail("Expected exception to be raised");
        } catch (IllegalStateException actual) {
            assertEquals(thrown, actual);
            assertEquals(0, count.get());
        }
    }

    @Test
    public void errorsInCleanupAreAddedToBodyErrors() {
        IllegalStateException bodyError    = new IllegalStateException("kaboom");
        IllegalStateException cleanupError = new IllegalStateException("KABOOM");
        IO<Integer> hashIO = bracket(io(count),
                                     c -> io(() -> {throw cleanupError;}),
                                     c -> io(() -> {throw bodyError;}));
        try {
            hashIO.unsafePerformIO();
            fail("Expected exception to be raised");
        } catch (IllegalStateException actual) {
            assertEquals(bodyError, actual);
            assertArrayEquals(new Throwable[]{cleanupError}, actual.getSuppressed());
        }
    }
}