package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.EqualityAwareIO;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.FunctorLaws;
import testsupport.traits.MonadLaws;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static com.jnape.palatable.lambda.functions.Fn0.fn0;
import static com.jnape.palatable.lambda.functions.IO.externallyManaged;
import static com.jnape.palatable.lambda.functions.IO.io;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Tupler2.tupler;
import static com.jnape.palatable.lambda.functions.specialized.checked.CheckedFn1.checked;
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
    public void zipAndDerivativesComposesInParallel() {
        String a = "foo";
        Fn1<String, Tuple2<Integer, String>> f = tupler(1);

        ExecutorService executor = newFixedThreadPool(2);
        CountDownLatch advanceFirst = new CountDownLatch(1);
        CountDownLatch advanceSecond = new CountDownLatch(1);

        IO<String> ioA = io(checked(__ -> {
            advanceFirst.countDown();
            advanceSecond.await();
            return a;
        }));
        IO<Function<? super String, ? extends Tuple2<Integer, String>>> ioF = io(checked(__ -> {
            advanceFirst.await();
            advanceSecond.countDown();
            return f;
        }));

        IO<Tuple2<Integer, String>> zip = ioA.zip(ioF);
        assertEquals(f.apply(a), zip.unsafePerformAsyncIO().join());
        assertEquals(f.apply(a), zip.unsafePerformAsyncIO(executor).join());
        assertEquals(f.apply(a), zip.unsafePerformAsyncIO(executor).join());

        IO<Function<? super String, ? extends Tuple2<Integer, String>>> discardL = ioA.discardL(ioF);
        assertEquals(f, discardL.unsafePerformAsyncIO().join());
        assertEquals(f, discardL.unsafePerformAsyncIO(executor).join());

        IO<String> discardR = ioA.discardR(ioF);
        assertEquals(a, discardR.unsafePerformAsyncIO().join());
        assertEquals(a, discardR.unsafePerformAsyncIO(executor).join());
    }

    @Test
    public void delegatesToExternallyManagedFuture() {
        CompletableFuture<Integer> future = completedFuture(1);
        IO<Integer> io = externallyManaged(() -> future);
        assertEquals((Integer) 1, io.unsafePerformIO());
        assertEquals((Integer) 1, io.unsafePerformAsyncIO().join());
        assertEquals((Integer) 1, io.unsafePerformAsyncIO(commonPool()).join());
    }

    @Test
    public void exceptionallyRecoversThrowableToResult() {
        IO<String> io = io(() -> { throw new UnsupportedOperationException("foo"); });
        assertEquals("foo", io.exceptionally(Throwable::getMessage).unsafePerformIO());

        IO<String> externallyManaged = externallyManaged(() -> new CompletableFuture<String>() {{
            completeExceptionally(new UnsupportedOperationException("foo"));
        }}).exceptionally(e -> e.getCause().getMessage());
        assertEquals("foo", externallyManaged.unsafePerformIO());
    }

    @Test
    public void exceptionallyRescuesFutures() {
        ExecutorService executor = newFixedThreadPool(2);

        IO<String> io = io(() -> { throw new UnsupportedOperationException("foo"); });
        assertEquals("foo", io.exceptionally(e -> e.getCause().getMessage()).unsafePerformAsyncIO().join());
        assertEquals("foo", io.exceptionally(e -> e.getCause().getMessage()).unsafePerformAsyncIO(executor).join());

        IO<String> externallyManaged = externallyManaged(() -> new CompletableFuture<String>() {{
            completeExceptionally(new UnsupportedOperationException("foo"));
        }}).exceptionally(e -> e.getCause().getMessage());
        assertEquals("foo", externallyManaged.unsafePerformIO());

    }
}