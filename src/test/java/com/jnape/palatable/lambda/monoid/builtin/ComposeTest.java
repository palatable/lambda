package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.monoid.Monoid;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.jnape.palatable.lambda.monoid.builtin.Compose.compose;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ComposeTest {

    @Test
    public void monoid() throws ExecutionException, InterruptedException {
        Monoid<Integer> addition = Monoid.monoid((x, y) -> x + y, 0);

        CompletableFuture<Integer> failedFuture = new CompletableFuture<Integer>() {{
            completeExceptionally(new RuntimeException());
        }};

        assertEquals((Integer) 0, compose(addition).identity().get());
        assertEquals((Integer) 3, compose(addition, completedFuture(1), completedFuture(2)).get());
        assertTrue(compose(addition, failedFuture, completedFuture(2)).isCompletedExceptionally());
        assertTrue(compose(addition, completedFuture(1), failedFuture).isCompletedExceptionally());
        assertTrue(compose(addition, failedFuture, failedFuture).isCompletedExceptionally());
    }
}