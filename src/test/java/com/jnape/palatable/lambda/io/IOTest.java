package com.jnape.palatable.lambda.io;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn2.Sequence;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.EquatableM;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.FunctorLaws;
import testsupport.traits.MonadLaws;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Size.size;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Replicate.replicate;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Tupler2.tupler;
import static com.jnape.palatable.lambda.functions.builtin.fn3.Times.times;
import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;
import static com.jnape.palatable.lambda.io.IO.externallyManaged;
import static com.jnape.palatable.lambda.io.IO.io;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.ForkJoinPool.commonPool;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static testsupport.Constants.STACK_EXPLODING_NUMBER;

@RunWith(Traits.class)
public class IOTest {

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, MonadLaws.class})
    public EquatableM<IO<?>, Integer> testSubject() {
        return new EquatableM<>(io(1), IO::unsafePerformIO);
    }

    @Test
    public void staticFactoryMethods() {
        assertEquals((Integer) 1, io(1).unsafePerformIO());
        assertEquals((Integer) 1, io(() -> 1).unsafePerformIO());
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
        String                               a = "foo";
        Fn1<String, Tuple2<Integer, String>> f = tupler(1);

        ExecutorService executor      = newFixedThreadPool(2);
        CountDownLatch  advanceFirst  = new CountDownLatch(1);
        CountDownLatch  advanceSecond = new CountDownLatch(1);

        IO<String> ioA = io(() -> {
            advanceFirst.countDown();
            advanceSecond.await();
            return a;
        });
        IO<Fn1<? super String, ? extends Tuple2<Integer, String>>> ioF = io(() -> {
            advanceFirst.await();
            advanceSecond.countDown();
            return f;
        });

        IO<Tuple2<Integer, String>> zip = ioA.zip(ioF);
        assertEquals(f.apply(a), zip.unsafePerformAsyncIO().join());
        assertEquals(f.apply(a), zip.unsafePerformAsyncIO(executor).join());
        assertEquals(f.apply(a), zip.unsafePerformAsyncIO(executor).join());

        IO<Fn1<? super String, ? extends Tuple2<Integer, String>>> discardL = ioA.discardL(ioF);
        assertEquals(f, discardL.unsafePerformAsyncIO().join());
        assertEquals(f, discardL.unsafePerformAsyncIO(executor).join());

        IO<String> discardR = ioA.discardR(ioF);
        assertEquals(a, discardR.unsafePerformAsyncIO().join());
        assertEquals(a, discardR.unsafePerformAsyncIO(executor).join());
    }

    @Test
    public void delegatesToExternallyManagedFuture() {
        CompletableFuture<Integer> future = completedFuture(1);
        IO<Integer>                io     = externallyManaged(() -> future);
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

    @Test
    public void safe() {
        assertEquals(right(1), io(() -> 1).safe().unsafePerformIO());
        IllegalStateException thrown = new IllegalStateException("kaboom");
        assertEquals(left(thrown), io(() -> {throw thrown;}).safe().unsafePerformIO());
    }

    @Test
    public void ensuring() {
        AtomicInteger counter    = new AtomicInteger(0);
        IO<Integer>   incCounter = io(counter::incrementAndGet);
        assertEquals("foo", io(() -> "foo").ensuring(incCounter).unsafePerformIO());
        assertEquals(1, counter.get());

        IllegalStateException thrown = new IllegalStateException("kaboom");
        try {
            io(() -> {throw thrown;}).ensuring(incCounter).unsafePerformIO();
            fail("Expected exception to have been thrown, but wasn't.");
        } catch (IllegalStateException actual) {
            assertEquals(thrown, actual);
            assertEquals(2, counter.get());
        }
    }

    @Test
    public void ensuringRunsStrictlyAfterIO() {
        Executor      twoThreads = Executors.newFixedThreadPool(2);
        AtomicInteger counter    = new AtomicInteger(0);
        io(() -> {
            Thread.sleep(100);
            counter.incrementAndGet();
        }).ensuring(io(() -> {
            if (counter.get() == 0)
                fail("Expected to run after initial IO, but ran first");
        })).unsafePerformAsyncIO(twoThreads).join();
    }

    @Test
    public void ensuringAttachesThrownExceptionToThrownBodyException() {
        IllegalStateException thrownByBody     = new IllegalStateException("kaboom");
        IllegalStateException thrownByEnsuring = new IllegalStateException("KABOOM");

        try {
            io(() -> {throw thrownByBody;}).ensuring(io(() -> {throw thrownByEnsuring;})).unsafePerformIO();
            fail("Expected exception to have been thrown, but wasn't.");
        } catch (IllegalStateException actual) {
            assertEquals(thrownByBody, actual);
            assertArrayEquals(new Throwable[]{thrownByEnsuring}, actual.getSuppressed());
        }
    }

    @Test
    public void throwing() {
        IllegalStateException expected = new IllegalStateException("thrown");
        try {
            IO.throwing(expected).unsafePerformIO();
        } catch (IllegalStateException actual) {
            assertEquals(expected, actual);
        }
    }

    @Test
    public void zipStackSafety() {
        IO<Fn1<? super Integer, ? extends Integer>> zipAdd1 = io(x -> x + 1);
        IO<Integer>                                 zero    = io(0);

        IO<Integer> leftHeavy = times(STACK_EXPLODING_NUMBER, io -> io.zip(zipAdd1), zero);
        assertEquals(STACK_EXPLODING_NUMBER, leftHeavy.unsafePerformIO());
        assertEquals(STACK_EXPLODING_NUMBER, leftHeavy.unsafePerformAsyncIO().join());

        IO<Integer> rightHeavy = times(STACK_EXPLODING_NUMBER, io -> zipAdd1.zip(io.fmap(x -> f -> f.apply(x))), zero);
        assertEquals(STACK_EXPLODING_NUMBER, rightHeavy.unsafePerformIO());
        assertEquals(STACK_EXPLODING_NUMBER, rightHeavy.unsafePerformAsyncIO().join());
    }

    @Test
    public void discardStackSafety() {
        IO<Integer> discardL = times(STACK_EXPLODING_NUMBER, io -> io(1).discardL(io), io(0));
        assertEquals((Integer) 0, discardL.unsafePerformIO());
        assertEquals((Integer) 0, discardL.unsafePerformAsyncIO().join());

        IO<Integer> discardR = times(STACK_EXPLODING_NUMBER, io -> io.discardR(io(1)), io(0));
        assertEquals((Integer) 0, discardR.unsafePerformIO());
        assertEquals((Integer) 0, discardR.unsafePerformAsyncIO().join());
    }

    @Test
    public void lazyZipStackSafety() {
        IO<Fn1<? super Integer, ? extends Integer>> zipAdd1 = io(x -> x + 1);
        IO<Integer>                                 zero    = io(0);

        IO<Integer> leftHeavy = times(STACK_EXPLODING_NUMBER, io -> io.lazyZip(lazy(zipAdd1)).value(), zero);
        assertEquals(STACK_EXPLODING_NUMBER, leftHeavy.unsafePerformIO());
        assertEquals(STACK_EXPLODING_NUMBER, leftHeavy.unsafePerformAsyncIO().join());

        IO<Integer> rightHeavy = times(STACK_EXPLODING_NUMBER,
                                       io -> zipAdd1.<Integer>lazyZip(lazy(io.fmap(x -> f -> f.apply(x)))).value(),
                                       zero);
        assertEquals(STACK_EXPLODING_NUMBER, rightHeavy.unsafePerformIO());
        assertEquals(STACK_EXPLODING_NUMBER, rightHeavy.unsafePerformAsyncIO().join());
    }

    @Test
    public void flatMapStackSafety() {
        Fn1<Integer, IO<Integer>> add1 = x -> io(() -> x + 1);
        IO<Integer>               zero = io(0);

        IO<Integer> leftHeavy = times(STACK_EXPLODING_NUMBER, io -> io.flatMap(add1), zero);
        assertEquals(STACK_EXPLODING_NUMBER, leftHeavy.unsafePerformIO());
        assertEquals(STACK_EXPLODING_NUMBER, leftHeavy.unsafePerformAsyncIO().join());

        IO<Integer> rightHeavy = times(STACK_EXPLODING_NUMBER,
                                       io -> add1.apply(0).flatMap(x -> io.fmap(y -> x + y)),
                                       zero);
        assertEquals(STACK_EXPLODING_NUMBER, rightHeavy.unsafePerformIO());
        assertEquals(STACK_EXPLODING_NUMBER, rightHeavy.unsafePerformAsyncIO().join());
    }

    @Test
    public void staggeredZipAndFlatMapStackSafety() {
        assertEquals(STACK_EXPLODING_NUMBER,
                     times(STACK_EXPLODING_NUMBER, io -> io.<Integer>zip(io(x -> x + 1))
                             .flatMap(x -> io(() -> x))
                             .zip(io(x -> x)), io(0))
                             .unsafePerformIO());
    }

    @Test
    public void sequenceStackSafety() {
        assertEquals(STACK_EXPLODING_NUMBER,
                     Sequence.sequence(replicate(STACK_EXPLODING_NUMBER, io(UNIT)), IO::io)
                             .fmap(size())
                             .fmap(Long::intValue)
                             .unsafePerformIO());

        assertEquals(STACK_EXPLODING_NUMBER,
                     Sequence.sequence(replicate(STACK_EXPLODING_NUMBER, io(UNIT)), IO::io)
                             .fmap(size())
                             .fmap(Long::intValue)
                             .unsafePerformAsyncIO().join());
    }

    @Test
    public void sequenceIOExecutesInParallel() {
        int            n     = 2;
        CountDownLatch latch = new CountDownLatch(n);
        Sequence.sequence(replicate(n, io(() -> {
            latch.countDown();
            if (!latch.await(100, MILLISECONDS)) {
                throw new AssertionError("Expected latch to countDown in time, but didn't.");
            }
        })), IO::io)
                .unsafePerformAsyncIO()
                .join();
    }

    @Test
    public void sequenceIsRepeatable() {
        IO<Iterable<Unit>> io = Sequence.sequence(replicate(3, io(UNIT)), IO::io);

        assertEquals((Long) 3L, size(io.unsafePerformIO()));
        assertEquals((Long) 3L, size(io.unsafePerformIO()));
    }
}