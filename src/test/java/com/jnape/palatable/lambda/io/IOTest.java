package com.jnape.palatable.lambda.io;

import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.EqualityAwareIO;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.FunctorLaws;
import testsupport.traits.MonadLaws;

import java.util.concurrent.CompletableFuture;

import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static com.jnape.palatable.lambda.functions.Fn0.fn0;
import static com.jnape.palatable.lambda.io.IO.externallyManaged;
import static com.jnape.palatable.lambda.io.IO.io;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.ForkJoinPool.commonPool;
import static org.junit.Assert.assertEquals;

@RunWith(Traits.class)
public class IOTest {

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, MonadLaws.class})
    public IO<Integer> testSubject() {
        return new EqualityAwareIO<>(io(1));
    }

    @Test
    public void staticFactoryMethods() {
        assertEquals((Integer) 1, io(1).unsafePerformIO());
        assertEquals((Integer) 1, io(() -> 1).unsafePerformIO());
        assertEquals((Integer) 1, io(fn0(() -> 1)).unsafePerformIO());
        assertEquals(UNIT, io(() -> {}).unsafePerformIO());
    }

    @Test(timeout = 100)
    public void unsafePerformAsyncIOWithoutExecutor() {
        assertEquals((Integer) 1, io(() -> 1).unsafePerformAsyncIO().join());
    }

    @Test(timeout = 100)
    public void unsafePerformAsyncIOWithExecutor() {
        assertEquals((Integer) 1, io(() -> 1).unsafePerformAsyncIO(newFixedThreadPool(1)).join());
    }

    @Test
    public void delegatesToExternallyManagedFuture() {
        CompletableFuture<Integer> future = completedFuture(1);
        IO<Integer> io = externallyManaged(() -> future);
        assertEquals((Integer) 1, io.unsafePerformIO());
        assertEquals((Integer) 1, io.unsafePerformAsyncIO().join());
        assertEquals((Integer) 1, io.unsafePerformAsyncIO(commonPool()).join());
    }
}