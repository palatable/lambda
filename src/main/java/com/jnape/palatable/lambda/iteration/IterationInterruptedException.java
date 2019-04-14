package com.jnape.palatable.lambda.iteration;

import com.jnape.palatable.lambda.functions.builtin.fn4.RateLimit;

import java.util.Iterator;

/**
 * An exception thrown when a thread is interrupted while an {@link Iterator} was blocked.
 *
 * @see RateLimit
 */
@SuppressWarnings("serial")
public final class IterationInterruptedException extends RuntimeException {

    public IterationInterruptedException(InterruptedException cause) {
        super(cause);
    }
}
