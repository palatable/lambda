package com.jnape.palatable.lambda.semigroup.builtin;

import com.jnape.palatable.lambda.semigroup.Semigroup;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.jnape.palatable.lambda.semigroup.builtin.Compose.compose;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ComposeTest {

    @Test
    public void semigroup() throws ExecutionException, InterruptedException {
        Semigroup<Integer> addition = (x, y) -> x + y;

        CompletableFuture<Integer> failedFuture = new CompletableFuture<Integer>() {{
            completeExceptionally(new RuntimeException());
        }};

        assertEquals((Integer) 3, compose(addition, completedFuture(1), completedFuture(2)).get());
        assertTrue(compose(addition, completedFuture(1), failedFuture).isCompletedExceptionally());
        assertTrue(compose(addition, failedFuture, completedFuture(1)).isCompletedExceptionally());
        assertTrue(compose(addition, failedFuture, failedFuture).isCompletedExceptionally());
    }
}