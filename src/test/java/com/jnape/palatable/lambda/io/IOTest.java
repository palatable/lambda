package com.jnape.palatable.lambda.io;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.hlist.HList;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.Equivalence;
import testsupport.traits.FunctorLaws;
import testsupport.traits.MonadLaws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Size.size;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Eq.eq;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Replicate.replicate;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Sequence.sequence;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Tupler2.tupler;
import static com.jnape.palatable.lambda.functions.builtin.fn3.LiftA2.liftA2;
import static com.jnape.palatable.lambda.functions.builtin.fn3.Times.times;
import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;
import static com.jnape.palatable.lambda.io.IO.externallyManaged;
import static com.jnape.palatable.lambda.io.IO.io;
import static com.jnape.palatable.lambda.io.IO.pureIO;
import static com.jnape.palatable.traitor.framework.Subjects.subjects;
import static java.lang.Thread.currentThread;
import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static java.util.concurrent.ForkJoinPool.commonPool;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static testsupport.Constants.STACK_EXPLODING_NUMBER;
import static testsupport.assertion.MonadErrorAssert.assertLawsEq;
import static testsupport.traits.Equivalence.equivalence;

@RunWith(Traits.class)
public class IOTest {

    public @Rule ExpectedException thrown = ExpectedException.none();

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, MonadLaws.class})
    public Equivalence<IO<Integer>> testSubject() {
        return equivalence(io(1), IO::unsafePerformIO);
    }

    @Test
    public void monadError() {
        assertLawsEq(subjects(equivalence(IO.throwing(new IllegalStateException("a")), IO::unsafePerformIO),
                              equivalence(io(1), IO::unsafePerformIO)),
                     new IOException("bar"),
                     e -> io(e.getMessage().length()));
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
    public void catchError() {
        Executor   executor = newFixedThreadPool(2);
        IO<String> io       = IO.throwing(new UnsupportedOperationException("foo"));
        assertEquals("foo", io.catchError(t -> io(t::getMessage)).unsafePerformIO());
        assertEquals("foo",
                     io.catchError(e -> io(() -> e.getCause().getMessage())).unsafePerformAsyncIO().join());
        assertEquals("foo",
                     io.catchError(e -> io(() -> e.getCause().getMessage()))
                             .unsafePerformAsyncIO(executor).join());

        IO<String> externallyManaged = externallyManaged(() -> new CompletableFuture<String>() {{
            completeExceptionally(new UnsupportedOperationException("foo"));
        }}).catchError(e -> io(() -> e.getCause().getMessage()));
        assertEquals("foo", externallyManaged.unsafePerformIO());
    }

    @Test
    public void catchAndRethrow() {
        IllegalStateException expected = new IllegalStateException("expected");
        IO<Object> catchAndRethrow = IO.throwing(expected)
                .catchError(IO::throwing);

        try {
            catchAndRethrow.unsafePerformIO();
        } catch (Exception actual) {
            assertSame(expected, actual);
        }

        try {
            catchAndRethrow.unsafePerformAsyncIO().join();
        } catch (CompletionException actual) {
            assertEquals(expected, actual.getCause());
        }
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
                     sequence(replicate(STACK_EXPLODING_NUMBER, io(UNIT)), IO::io)
                             .fmap(size())
                             .fmap(Long::intValue)
                             .unsafePerformIO());

        assertEquals(STACK_EXPLODING_NUMBER,
                     sequence(replicate(STACK_EXPLODING_NUMBER, io(UNIT)), IO::io)
                             .fmap(size())
                             .fmap(Long::intValue)
                             .unsafePerformAsyncIO().join());
    }

    @Test
    public void sequenceIOExecutesInParallel() {
        int            n     = 2;
        CountDownLatch latch = new CountDownLatch(n);
        sequence(replicate(n, io(() -> {
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
        IO<Iterable<Unit>> io = sequence(replicate(3, io(UNIT)), IO::io);

        assertEquals((Long) 3L, size(io.unsafePerformIO()));
        assertEquals((Long) 3L, size(io.unsafePerformIO()));
    }

    @Test(expected = InterruptedException.class)
    public void interruptible() {
        currentThread().interrupt();

        IO<Unit> io = IO.interruptible(IO.throwing(new AssertionError("expected to never be called")));
        io.unsafePerformIO();
    }

    @Test
    public void monitorSync() throws InterruptedException {
        Object         lock       = new Object();
        List<String>   accesses   = new ArrayList<>();
        CountDownLatch oneStarted = new CountDownLatch(1);
        CountDownLatch finishLine = new CountDownLatch(2);

        IO<Unit> one = IO.monitorSync(lock, io(() -> {
            accesses.add("one entered");
            oneStarted.countDown();
            Thread.sleep(10);
            accesses.add("one exited");
            finishLine.countDown();
        }));

        IO<Unit> two = IO.monitorSync(lock, io(() -> {
            oneStarted.await();
            accesses.add("two entered");
            accesses.add("two exited");
            finishLine.countDown();
        }));

        new Thread(one::unsafePerformIO) {{
            start();
        }};

        new Thread(two::unsafePerformIO) {{
            start();
        }};

        if (!finishLine.await(15, SECONDS))
            fail("Expected threads to have completed by now, only got this far: " + accesses);
        assertEquals(asList("one entered", "one exited", "two entered", "two exited"), accesses);
    }

    @Test
    public void fuse() {
        IO<Thread>                 currentThreadIO = io(Thread::currentThread);
        IO<Tuple2<Thread, Thread>> threads         = liftA2(HList::tuple, currentThreadIO, currentThreadIO);
        Executor                   executor        = Executors.newFixedThreadPool(2);
        Boolean                    sameThread      = IO.fuse(threads).unsafePerformAsyncIO(executor).join().into(eq());
        assertTrue("Expected both IOs to run on the same Thread, but they didn't.", sameThread);
    }

    @Test
    public void pin() {
        Thread     mainThread      = currentThread();
        IO<Thread> currentThreadIO = io(Thread::currentThread);
        Executor   executor        = Executors.newFixedThreadPool(2);
        Thread     chosenThread    = IO.pin(currentThreadIO, Runnable::run).unsafePerformAsyncIO(executor).join();
        assertEquals("Expected IO to run on the main Thread, but it didn't.", mainThread, chosenThread);
    }

    @Test
    public void memoize() {
        AtomicInteger counter  = new AtomicInteger(0);
        IO<Integer>   memoized = IO.memoize(io(counter::incrementAndGet));
        memoized.unsafePerformIO();
        memoized.unsafePerformIO();
        assertEquals(1, counter.get());
    }

    @Test
    public void memoizationMutuallyExcludesSimultaneousComputation() throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);

        IO<Integer> memoized = IO.memoize(io(() -> {
            Thread.sleep(10);
            return counter.incrementAndGet();
        }));

        Thread t1 = new Thread(memoized::unsafePerformIO) {{
            start();
        }};

        Thread t2 = new Thread(memoized::unsafePerformIO) {{
            start();
        }};

        t1.join();
        t2.join();

        assertEquals(1, counter.get());
    }

    @Test
    public void failuresAreNotMemoized() {
        IllegalStateException exception = new IllegalStateException("not yet");
        AtomicInteger         counter   = new AtomicInteger(0);
        IO<Integer> io = IO.memoize(io(() -> {
            int next = counter.incrementAndGet();
            if (next > 1)
                return next;
            throw exception;
        }));

        assertEquals(left(exception), io.safe().unsafePerformIO());
        assertEquals((Integer) 2, io.unsafePerformIO());
        assertEquals((Integer) 2, io.unsafePerformIO());
    }

    @Test
    public void staticPure() {
        IO<Integer> io = pureIO().apply(1);
        assertEquals((Integer) 1, io.unsafePerformIO());
    }

    @Test
    public void zipExecutionOrdering() {
        List<String> invocationsSync = new ArrayList<>();
        io(() -> invocationsSync.add("one"))
                .zip(io(() -> invocationsSync.add("two"))
                             .zip(io(() -> invocationsSync.add("three")).fmap(x -> y -> z -> z)))
                .unsafePerformIO();
        assertEquals(asList("one", "two", "three"), invocationsSync);

        List<String> invocationsAsync = new ArrayList<>();
        io(() -> invocationsAsync.add("one"))
                .zip(io(() -> invocationsAsync.add("two"))
                             .zip(io(() -> invocationsAsync.add("three")).fmap(x -> y -> z -> z)))
                .unsafePerformAsyncIO(newSingleThreadExecutor()).join();
        assertEquals(asList("one", "two", "three"), invocationsSync);
    }

    @Test
    public void discardLExecutionOrdering() {
        List<String> invocationsSync = new ArrayList<>();
        io(() -> invocationsSync.add("one"))
                .discardL(io(() -> invocationsSync.add("two")))
                .discardL(io(() -> invocationsSync.add("three")))
                .unsafePerformIO();
        assertEquals(asList("one", "two", "three"), invocationsSync);

        List<String> invocationsAsync = new ArrayList<>();
        io(() -> invocationsAsync.add("one"))
                .discardL(io(() -> invocationsAsync.add("two")))
                .discardL(io(() -> invocationsAsync.add("three")))
                .unsafePerformAsyncIO(newSingleThreadExecutor())
                .join();
        assertEquals(asList("one", "two", "three"), invocationsSync);
    }

    @Test
    public void discardRExecutionOrdering() {
        List<String> invocationsSync = new ArrayList<>();
        io(() -> invocationsSync.add("one"))
                .discardR(io(() -> invocationsSync.add("two")))
                .discardR(io(() -> invocationsSync.add("three")))
                .unsafePerformIO();
        assertEquals(asList("one", "two", "three"), invocationsSync);

        List<String> invocationsAsync = new ArrayList<>();
        io(() -> invocationsAsync.add("one"))
                .discardR(io(() -> invocationsAsync.add("two")))
                .discardR(io(() -> invocationsAsync.add("three")))
                .unsafePerformAsyncIO(newSingleThreadExecutor())
                .join();
        assertEquals(asList("one", "two", "three"), invocationsSync);
    }
}